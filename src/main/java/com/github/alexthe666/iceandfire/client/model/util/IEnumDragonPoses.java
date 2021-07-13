package com.github.alexthe666.iceandfire.client.model.util;

/**
 * Common interface for all dragon pose enumerations. If you are an addon author, you will need to create an enumeration which implements this interface. Then register it into the {@link DragonAnimationsLibrary} with an enumeration of model types
 * @see IEnumDragonModelTypes
 */
public interface IEnumDragonPoses {
    /**
     *
     * @return a {@link String} representing the pose. Should match the filename suffix of the pose .tbl
     */
    public String getPose();
}
