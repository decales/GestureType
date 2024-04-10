package com.example.project.model.bluetooth

object KeyboardDescriptors {

    val standard = listOf(
        0x05, 0x01, // Usage Page (Generic Desktop)
        0x09, 0x06, // Usage (Keyboard)
        0xA1, 0x01, // Collection (Application)
        0x05, 0x07, // Usage Page (Key Codes)
        0x19, 0xe0, // Usage Minimum (224)
        0x29, 0xe7, // Usage Maximum (231)
        0x15, 0x00, // Logical Minimum (0)
        0x25, 0x01, // Logical Maximum (1)
        0x75, 0x01, // Report Size (1)
        0x95, 0x08, // Report Count (8)
        0x81, 0x02, // Input (Data, Variable, Absolute)

        0x95, 0x01, // Report Count (1)
        0x75, 0x08, // Report Size (8)
        0x81, 0x01, // Input (Constant) reserved byte(1)

        0x95, 0x05, // Report Count (5)
        0x75, 0x01, // Report Size (1)
        0x05, 0x08, // Usage Page (Page# for LEDs)
        0x19, 0x01, // Usage Minimum (1)
        0x29, 0x05, // Usage Maximum (5)
        0x91, 0x02, // Output (Data, Variable, Absolute), Led report
        0x95, 0x01, // Report Count (1)
        0x75, 0x03, // Report Size (3)
        0x91, 0x01, // Output (Data, Variable, Absolute), Led report padding

        0x95, 0x06, // Report Count (6)
        0x75, 0x08, // Report Size (8)
        0x15, 0x00, // Logical Minimum (0)
        0x25, 0x65, // Logical Maximum (101)
        0x05, 0x07, // Usage Page (Key codes)
        0x19, 0x00, // Usage Minimum (0)
        0x29, 0x65, // Usage Maximum (101)
        0x81, 0x00, // Input (Data, Array) Key array(6 bytes)
        0xC0        // End Collection (Application)
    ).map { it.toByte() }.toByteArray()

    val modified = listOf(
        0x05, 0x01, // Usage Page (Generic Desktop)
        0x09, 0x06, // Usage (Keyboard)
        0xA1, 0x01, // Collection (Application)
        0x85, 0x08, // REPORT_ID (Keyboard)
        0x05, 0x07, // Usage Page (Key Codes)
        0x19, 0xe0, // Usage Minimum (224)
        0x29, 0xe7, // Usage Maximum (231)
        0x15, 0x00, // Logical Minimum (0)
        0x25, 0x01, // Logical Maximum (1)
        0x75, 0x01, // Report Size (1)
        0x95, 0x08, // Report Count (8)
        0x81, 0x02, // Input (Data, Variable, Absolute)

        0x95, 0x01, // Report Count (1)
        0x75, 0x08, // Report Size (8)
        0x81, 0x01, // Input (Constant) reserved byte(1)

        0x95, 0x01, // Report Count (1)
        0x75, 0x08, // Report Size (8)
        0x15, 0x00, // Logical Minimum (0)
        0x25, 0x65, // Logical Maximum (101)
        0x05, 0x07, // Usage Page (Key codes)
        0x19, 0x00, // Usage Minimum (0)
        0x29, 0x65, // Usage Maximum (101)
        0x81, 0x00, // Input (Data, Array) Key array(6 bytes)
        0xC0        // End Collection (Application)
    ).map { it.toByte() }.toByteArray()
}

