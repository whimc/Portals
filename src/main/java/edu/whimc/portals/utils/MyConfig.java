package edu.whimc.portals.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This class manages the plugin's config file.
 */
public class MyConfig {

	/* The number of comments. */
	private int comments;
	/* An instance of a MyConfigManager. */
	private MyConfigManager manager;

	/* The configuration file. */
	private File file;
	/* The configuration file as a FileConfiguration. */
	private FileConfiguration config;

	/**
	 * Constructs a MyConfig.
	 *
	 * @param configFile The configuration file.
	 * @param comments The number of comments.
	 * @param plugin The instance of the plugin.
	 */
	public MyConfig(/*InputStream configStream,*/ File configFile, int comments, JavaPlugin plugin) {
		this.comments = comments;
		this.manager = new MyConfigManager(plugin);
		this.file = configFile;
		this.config = YamlConfiguration.loadConfiguration(configFile);
	}

	/**
	 * Gets the Object for the specified config path.
	 *
	 * @param path The path in the config file.
	 * @return The Object at the specified config path.
	 */
	public Object get(String path) { return this.config.get(path); }

	/**
	 * Gets the Object for the specified config path. Will give the specified default value if the
	 * path cannot be found.
	 *
	 * @param path The path in the config file.
	 * @param def The default value to return if the path is not found.
	 * @return The Object at the specified config path or the specified default value.
	 */
	public Object get(String path, Object def) { return this.config.get(path, def); }

	/**
	 * Get a String type from the current path.
	 *
	 * @param path The path in the config file.
	 * @return The string read from the config file.
	 */
	public String getString(String path) { return this.config.getString(path); }

	/**
	 * Get a String type from the current path. Will give the specified default value if the
	 * path cannot be found.
	 *
	 * @param path The path in the config file.
	 * @param def The default value to return if the path is not found.
	 * @return The string at the specified config path or the specified default value.
	 */
	public String getString(String path, String def) { return this.config.getString(path, def); }

	/**
	 * Get a Integer type from the current path.
	 *
	 * @param path The path in the config file.
	 * @return The integer read from the config file.
	 */
	public int getInt(String path) { return this.config.getInt(path); }

	/**
	 * Get a Integer type from the current path. Will give the specified default value if the
	 * path cannot be found.
	 *
	 * @param path The path in the config file.
	 * @param def The default value to return if the path is not found.
	 * @return The integer read from the config file or the specified default value.
	 */
	public int getInt(String path, int def) { return this.config.getInt(path, def); }

	/**
	 * Get a Long type from the current path.
	 *
	 * @param path The path in the config file.
	 * @return The long read from the config file.
	 */
	public long getLong(String path) { return this.config.getLong(path); }

	/**
	 * Get a Boolean type from the current path.
	 *
	 * @param path The path in the config file.
	 * @return The boolean read from the config file.
	 */
	public boolean getBoolean(String path) { return this.config.getBoolean(path); }

	/**
	 * Get a Boolean type from the current path. Will give the specified default value if the
	 * path cannot be found.
	 *
	 * @param path The path in the config file.
	 * @param def The default value to return if the path is not found.
	 * @return The boolean read from the config file or the specified default value.
	 */
	public boolean getBoolean(String path, boolean def) { return this.config.getBoolean(path, def); }

	/**
	 * Creates a section in the config with the given path.
	 *
	 * @param path The path for the new section in the config file.
	 */
	public void createSection(String path) { this.config.createSection(path); }

	/**
	 * Get the section of the configuration specified by the current path.
	 *
	 * @param path The path in the config file.
	 * @return The requested ConfigurationSection of the config.
	 */
	public ConfigurationSection getConfigurationSection(String path) { return this.config.getConfigurationSection(path); }

	/**
	 * Get a Double type from the current path.
	 *
	 * @param path The path in the config file.
	 * @return
	 */
	public double getDouble(String path) { return this.config.getDouble(path); }

	/**
	 * Get a Double type from the current path. Will give the specified default value if the
	 * path cannot be found.
	 *
	 * @param path The path in the config file.
	 * @param def The default value to return if the path is not found.
	 * @return The double read from the config file or the specified default value.
	 */
	public double getDouble(String path, double def) { return this.config.getDouble(path, def); }

	/**
	 * Get a Float type from the current path.
	 *
	 * @param path The path in the config file.
	 * @return The float read from the config file.
	 */
	public float getFloat(String path) { return Float.valueOf(this.config.getString(path)); }

	/**
	 * Get a List from the current path.
	 *
	 * @param path The path in the config file.
	 * @return The list read from the config file.
	 */
	public List<?> getList(String path) { return this.config.getList(path); }

	/**
	 * Get a List from the current path. Will give the specified default value if the
	 * path cannot be found.
	 *
	 * @param path The path in the config file.
	 * @param def The default value to return if the path is not found.
	 * @return The list read from the config file or the specified default value.
	 */
	public List<?> getList(String path, List<?> def) { return this.config.getList(path, def); }

	/**
	 * Get a List of strings from the current path.
	 *
	 * @param path The path in the config file.
	 * @return The list of strings read from the config file.
	 */
	public List<String> getStringList(String path) { return this.config.getStringList(path); }

	/**
	 * Check if the section with the given path is contained within the config file.
	 *
	 * @param path The path in the config file.
	 * @return Whether or not the section with the given path exists in the config file.
	 */
	public boolean contains(String path) { return this.config.contains(path); }

	/**
	 * Removes the specified path in the config file.
	 *
	 * @param path The path in the config file.
	 */
	public void removeKey(String path) { this.config.set(path, null); }

	/** @return The set of keys in the config. */
	public Set<String> getKeys() { return this.config.getKeys(false); }

	/**
	 * Sets the specified path to the given value.
	 *
	 * @param path The path in the config file.
	 * @param value The new value to set the path to.
	 */
	public void set(String path, Object value) { this.config.set(path, value); }

	/**
	 * Sets the specified path to the given value. Adds the specified comment if the path cannot be
	 * found.
	 *
	 * @param path The path in the config file.
	 * @param value The new value to set the path to.
	 * @param comment The comment to add when the path cannot be found.
	 */
	public void set(String path, Object value, String comment) {
		if (!this.config.contains(path)) {
			this.config.set(manager.getPluginName() + "_COMMENT_" + comments, " " + comment);
			comments++;
		}
		this.config.set(path, value);
	}

	/**
	 * Sets the specified path to the given value. Adds the specified comments if the path cannot be
	 * found.
	 *
	 * @param path The path in the config file.
	 * @param value
	 * @param comment The comments to add when the path cannot be found.
	 */
	public void set(String path, Object value, String[] comment) {
		for (String comm : comment) {
			if (!this.config.contains(path)) {
				this.config.set(manager.getPluginName() + "_COMMENT_" + comments, " " + comm);
				comments++;
			}
		}
		this.config.set(path, value);
	}

	/**
	 * Sets the header of the file.
	 *
	 * @param header The new header.
	 */
	public void setHeader(String[] header) {
		manager.setHeader(this.file, header);
		this.comments = header.length + 2;
		this.reloadConfig();
	}

	/**
	 * Reloads the current configuration from the config file.
	 */
	public void reloadConfig() {
		try {
			config.save(file);
			config.load(file);
		} catch (IOException | InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//this.config = YamlConfiguration.loadConfiguration(file);
		//this.config = YamlConfiguration.loadConfiguration(manager.getConfigContent(file));
	}

	/**
	 * Saves the current configuration to the config file.
	 */
	public void saveConfig() {
		String config = this.config.saveToString();
		manager.saveConfig(config, this.file);
	}
}