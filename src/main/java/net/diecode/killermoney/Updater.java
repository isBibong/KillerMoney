package net.diecode.killermoney;

import net.diecode.killermoney.enums.KMPermission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * This class is a barebones example of how to use the BukkitDev ServerMods API to check for file updates.
 * <br>
 * See the README file for further information of use.
 */
public class Updater implements Listener {

    // The project's unique ID
    private final int projectID = 55732;

    // An optional API key to use, will be null if not submitted
    private final String apiKey = "7b85388e796e6738738fe172679c8801d4cc2d74";

    // Keys for extracting file information from JSON response
    private static final String API_NAME_VALUE = "name";
    private static final String API_LINK_VALUE = "downloadUrl";
    private static final String API_RELEASE_TYPE_VALUE = "releaseType";
    private static final String API_FILE_NAME_VALUE = "fileName";
    private static final String API_GAME_VERSION_VALUE = "gameVersion";

    // Static information for querying the API
    private static final String API_QUERY = "/servermods/files?projectIds=";
    private static final String API_HOST = "https://api.curseforge.com";

    private static boolean updateAvailable = false;

    String newestVersion;

    /**
     * Query the API to find the latest approved file's details.
     */
    public void query() {
        URL url = null;

        try {
            // Create the URL to query using the project's ID
            url = new URL(API_HOST + API_QUERY + projectID);
        } catch (MalformedURLException e) {
            // There was an error creating the URL

            e.printStackTrace();
            return;
        }

        try {
            // Open a connection and query the project
            URLConnection conn = url.openConnection();

            if (apiKey != null) {
                // Add the API key to the request if present
                conn.addRequestProperty("X-API-Key", apiKey);
            }

            // Add the user-agent to identify the program
            conn.addRequestProperty("User-Agent", "ServerModsAPI-KillerMoney");

            // Read the response of the query
            // The response will be in a JSON format, so only reading one line is necessary.
            final BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = reader.readLine();

            // Parse the array of files from the query's response
            JSONArray array = (JSONArray) JSONValue.parse(response);

            if (array.size() > 0) {
                // Get the newest file's details
                JSONObject latest = (JSONObject) array.get(array.size() - 1);

                // Get the version's title
                String versionName = (String) latest.get(API_NAME_VALUE);

                // Get the version's link
                String versionLink = (String) latest.get(API_LINK_VALUE);

                // Get the version's release type
                String versionType = (String) latest.get(API_RELEASE_TYPE_VALUE);

                // Get the version's file name
                String versionFileName = (String) latest.get(API_FILE_NAME_VALUE);

                // Get the version's game version
                String versionGameVersion = (String) latest.get(API_GAME_VERSION_VALUE);

                newestVersion = versionName.replaceAll("[^\\d.]", "");

                Bukkit.getScheduler().scheduleSyncDelayedTask(BukkitMain.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (isNewest(newestVersion)) {
                                updateAvailable = true;

                                Logger.info("------------------------------");
                                Logger.info("| Update found!");
                                Logger.info("| Your version: " +
                                        BukkitMain.getInstance().getDescription().getVersion() +
                                        " | " + "New version: " + newestVersion);
                                Logger.info("| " + BukkitMain.getInstance().getDescription().getWebsite());
                                Logger.info("------------------------------");

                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    if (player.isOp() || player.hasPermission(KMPermission.ADMIN.get())) {
                                        player.sendMessage(ChatColor.DARK_AQUA + " --- " + ChatColor.AQUA + "KillerMoney Update available" + ChatColor.DARK_AQUA + " --- ");
                                        player.sendMessage(ChatColor.DARK_AQUA + "> Your version: " + BukkitMain.getInstance().getDescription().getVersion() + ChatColor.GRAY + " | " + ChatColor.DARK_AQUA + "New version: " + newestVersion);
                                        player.sendMessage(ChatColor.DARK_AQUA + "> You can download the latest version from ");
                                        player.sendMessage(ChatColor.DARK_AQUA + "> " + ChatColor.AQUA + BukkitMain.getInstance().getDescription().getWebsite() );
                                        player.sendMessage(ChatColor.DARK_AQUA + " --- ");
                                    }
                                }
                            } else {
                                // newest version
                                Logger.info("Update not found");
                            }
                        } catch (NumberFormatException e) {
                            Logger.info("Update not found");
                        }
                    }
                });
            } else {
                System.out.println("There are no files for this project");
            }
        } catch (IOException e) {
            // There was an error reading the query
            System.out.println("Update check failed. Please try again later.");
        }
    }

    public static boolean isUpdateAvailable() {
        return updateAvailable;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!isUpdateAvailable()) {
            return;
        }

        final Player player = event.getPlayer();

        if (player.isOp() || player.hasPermission(KMPermission.ADMIN.get())) {
            Bukkit.getScheduler().runTaskLater(BukkitMain.getInstance(), new Runnable() {
                @Override
                public void run() {
                    player.sendMessage(ChatColor.DARK_AQUA + " --- " + ChatColor.AQUA + "KillerMoney Update available" + ChatColor.DARK_AQUA + " --- ");
                    player.sendMessage(ChatColor.DARK_AQUA + "> Your version: " + BukkitMain.getInstance().getDescription().getVersion() + ChatColor.GRAY + " | " + ChatColor.DARK_AQUA + "New version: " + newestVersion);
                    player.sendMessage(ChatColor.DARK_AQUA + "> You can download the latest version from ");
                    player.sendMessage(ChatColor.DARK_AQUA + "> " + ChatColor.AQUA + BukkitMain.getInstance().getDescription().getWebsite() );
                    player.sendMessage(ChatColor.DARK_AQUA + " --- ");
                }
            }, 5L);

        }
    }

    public static boolean isNewest(String version) {
        int[] updateVersion = new int[3];
        int[] currentVersion = new int[3];
        String[] newestVersion = version.split("\\.");
        String[] currentNumbers = BukkitMain.getInstance().getDescription().getVersion()
                .replaceAll("[^\\d.]", "").split("\\.");

        for (int i = 0; i < newestVersion.length; i++) {
            updateVersion[i] = Integer.parseInt(newestVersion[i]);
            currentVersion[i] = Integer.parseInt(currentNumbers[i]);
        }

        if (updateVersion[0] > currentVersion[0]) {
            return true;
        }

        if ((updateVersion[0] == currentVersion[0]) && updateVersion[1] > currentVersion[1]) {
            return true;
        }

        if ((updateVersion[0] == currentVersion[0]) && (updateVersion[1] == currentVersion[1]) && (updateVersion[2]) > (currentVersion[2])) {
            return true;
        }

        return false;
    }
}