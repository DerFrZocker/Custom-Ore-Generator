package de.derfrzocker.custom.ore.generator.api;

public interface ChunkInfo {

    /**
     * @param x positions must between 0 and 15
     * @param z positions must between 0 and 15
     * @return the y coordinate of the highest block on the given x and z coordinate
     * @throws IllegalArgumentException if x or z are not between 0 and 15
     */
    int getHighestBlock(int x, int z);

}
