/*
 * MIT License
 *
 * Copyright (c) 2019 DerFrZocker
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package de.derfrzocker.custom.ore.generator.api;

/**
 * Some standard OreSettings
 */
public final class OreSettings {

    public final static OreSetting VEIN_SIZE = OreSetting.createOreSetting("VEIN_SIZE");
    public final static OreSetting VEINS_PER_CHUNK = OreSetting.createOreSetting("VEINS_PER_CHUNK");
    public final static OreSetting HEIGHT_RANGE = OreSetting.createOreSetting("HEIGHT_RANGE");
    public final static OreSetting HEIGHT_CENTER = OreSetting.createOreSetting("HEIGHT_CENTER");
    public final static OreSetting MINIMUM_ORES_PER_CHUNK = OreSetting.createOreSetting("MINIMUM_ORES_PER_CHUNK");
    public final static OreSetting ORES_PER_CHUNK_RANGE = OreSetting.createOreSetting("ORES_PER_CHUNK_RANGE");
    public final static OreSetting MINIMUM_HEIGHT = OreSetting.createOreSetting("MINIMUM_HEIGHT");
    public final static OreSetting HEIGHT_SUBTRACT_VALUE = OreSetting.createOreSetting("HEIGHT_SUBTRACT_VALUE");

    private OreSettings() {
    }

}
