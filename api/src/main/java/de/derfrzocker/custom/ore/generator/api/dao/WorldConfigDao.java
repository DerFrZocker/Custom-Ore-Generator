package de.derfrzocker.custom.ore.generator.api.dao;

import de.derfrzocker.custom.ore.generator.api.WorldConfig;
import de.derfrzocker.spigot.utils.dao.BasicDao;

/**
 * Handel saving and loading from WorldConfig's
 * key: name of the World
 * value: the corresponding WorldOreConfig
 */
public interface WorldConfigDao extends BasicDao<String, WorldConfig> {

}
