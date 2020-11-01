/*
 * MIT License
 *
 * Copyright (c) 2019 - 2020 Marvin (DerFrZocker)
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

package de.derfrzocker.custom.ore.generator.impl.customdata;


import de.derfrzocker.custom.ore.generator.api.Info;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomDataApplier;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomDataType;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Function;

public abstract class FileReadAbleCustomData<T extends CustomDataApplier> extends AbstractCustomData<T> {

    @NotNull
    private final File fileFolder;

    public FileReadAbleCustomData(@NotNull final String name, @NotNull final Function<String, Info> infoFunction, @NotNull final File fileFolder) {
        super(name, CustomDataType.STRING, infoFunction); // FileReadAbleCustomData works only for String CustomData

        Validate.notNull(fileFolder, "File folder can not be null");

        if (!fileFolder.exists()) {
            if (!fileFolder.mkdirs()) {
                throw new RuntimeException("Can not create folder " + fileFolder);
            }
        }

        Validate.isTrue(fileFolder.isDirectory(), "File folder is not a directory");

        this.fileFolder = fileFolder;
    }

    @NotNull
    @Override
    public final CustomDataType getCustomDataType() { // Make it final, that no customData can override it.
        return CustomDataType.STRING;
    }

    @Override
    public final boolean isValidCustomData(@NotNull final Object customData, @NotNull final OreConfig oreConfig) { //final for security reason
        if (!(customData instanceof String))
            return false;

        final String data = (String) customData;

        if (data.startsWith("file:")) {

            final String readData = getDataFromFile(data);

            if (readData == null)
                return false;

            return isValidCustomData0(readData, oreConfig);
        }

        return isValidCustomData0(data, oreConfig);
    }

    @NotNull
    @Override
    public final String normalize(@NotNull final Object customData, @NotNull final OreConfig oreConfig) { //final for security reason
        final String data = (String) customData;

        if (!data.startsWith("file:")) {
            return normalize0(data, oreConfig);
        }

        final String newData = getDataFromFile(data);

        if (newData == null) {
            throw new RuntimeException("Returned Data for " + data + " is null");
        }

        return normalize0(newData, oreConfig);
    }

    protected abstract boolean isValidCustomData0(@NotNull final String customData, @NotNull final OreConfig oreConfig);

    protected String normalize0(@NotNull final String customData, @NotNull final OreConfig oreConfig) {
        return customData;
    }

    @Nullable
    private String getDataFromFile(@NotNull final String data) {
        final File file = new File(fileFolder, data.replace("file:", ""));

        if (!file.isFile()) {
            return null;
        }

        if (!isSubDirectory(file)) {
            return null;
        }

        try {
            final byte[] encoded = Files.readAllBytes(Paths.get(file.toURI()));
            return new String(encoded);
        } catch (final IOException e) {
            throw new RuntimeException("Unexpected error while reading String from " + data, e);
        }

    }

    private boolean isSubDirectory(@NotNull final File file) {
        File folder;
        File baseFolder;

        try {
            folder = file.getCanonicalFile().getParentFile();
            baseFolder = fileFolder.getCanonicalFile();
        } catch (final IOException e) {
            e.printStackTrace();
            return false;
        }

        while (folder != null) {
            if (baseFolder.equals(folder)) {
                return true;
            }

            folder = folder.getParentFile();
        }

        return false;
    }

}
