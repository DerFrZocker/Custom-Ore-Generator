package de.derfrzocker.custom.ore.generator.api.dao;

import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.spigot.utils.dao.BasicDao;

/**
 * Handel saving and loading from OreConfig's
 * key: name of the dao
 * value: the corresponding OreConfig
 */
public interface OreConfigDao extends BasicDao<String, OreConfig> {

}
