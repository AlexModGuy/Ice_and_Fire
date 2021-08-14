package com.github.alexthe666.iceandfire.client.model.util;

/**
 * Common interface for all dragon model type enumerations. If you are an addon author adding new dragon types, you will need to create an enumeration which implements this interface. Then register it into the {@link DragonAnimationsLibrary} with an enumeration of poses.<br/>
 * @see IEnumDragonPoses
 *
 */
public interface IEnumDragonModelTypes {
    /**
     * @return a {@link String} representing the model type. Should match the name of the folder containing this model type's poses
     */
    public String getModelType();
}
