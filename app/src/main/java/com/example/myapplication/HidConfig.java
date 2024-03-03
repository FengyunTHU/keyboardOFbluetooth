package com.example.myapplication;

// 蓝牙键盘的描述协议
public class HidConfig {
    public final static String KEYBOARD_NAME = "My Keyboard";
    public final static String DESCRIPTION = "KKKey";
    public final static String PROVIDER = "Alphabet";
    public final static byte ID_KEYBOARD = 1;

    // HID码表【不知道干啥的】
    public static final byte[] KEYBOARD_COMBO =
        {

//                (byte) 0x05, (byte) 0x01, //USAGE_PAGE (Generic Desktop)
//                (byte) 0x09, (byte) 0x06, //USAGE (Keyboard)
//                (byte) 0xA1, (byte) 0x01, //COLLECTION (Application)
//                (byte) 0x85, (byte) 0x08, //REPORT_ID (8)
//                (byte) 0x05, (byte) 0x07, //USAGE_PAGE (Keyboard)
//                (byte) 0x19, (byte) 0xE0, //USAGE_MINIMUM (Keyboard LeftControl)
//                (byte) 0x29, (byte) 0xE7, //USAGE_MAXIMUM (Keyboard Right GUI)
//                (byte) 0x15, (byte) 0x00, //LOGICAL_MINIMUM (0)
//                (byte) 0x25, (byte) 0x01, //LOGICAL_MAXIMUM (1)
//                //第一个字节
//                (byte) 0x75, (byte) 0x01, //REPORT_SIZE (1)
//                (byte) 0x95, (byte) 0x08, //REPORT_COUNT (8)
//                (byte) 0x81, (byte) 0x02, //INPUT (Data,Var,Abs)
//                //第二个字节
//                (byte) 0x95, (byte) 0x01, //REPORT_COUNT (1)
//                (byte) 0x75, (byte) 0x08, //REPORT_SIZE (8)
//                (byte) 0x81, (byte) 0x03, //INPUT (Const,Var,Abs)
//                //后六个字节
//                (byte) 0x95, (byte) 0x06, //REPORT_COUNT (6)
//                (byte) 0x75, (byte) 0x08, //REPORT_SIZE (8)
//                (byte) 0x15, (byte) 0x00, //LOGICAL_MINIMUM (0)
//                (byte) 0x25, (byte) 0x65, //LOGICAL_MAXIMUM (101)
//                (byte) 0x05, (byte) 0x07, //USAGE_PAGE (Keyboard)
//                (byte) 0x19, (byte) 0x00, //USAGE_MINIMUM (Reserved (no event indicated))
//                (byte) 0x29, (byte) 0x65, //USAGE_MAXIMUM (Keyboard Application)
//                (byte) 0x81, (byte) 0x00, //INPUT (Data,Ary,Abs)
//                (byte) 0xC0  //END_COLLECTION
//
//
                (byte) 0x05, (byte) 0x01,                         // Usage Page (Generic Desktop)
                (byte) 0x09, (byte) 0x06,                         // Usage (Keyboard)
                (byte) 0xA1, (byte) 0x01,                         // Collection (Application)
                (byte) 0x85, (byte) 0x08,                         //   REPORT_ID (Keyboard)
                (byte) 0x05, (byte) 0x07,                         //   Usage Page (Key Codes)
                (byte) 0x19, (byte) 0xE0,                         //   Usage Minimum (224)
                (byte) 0x29, (byte) 0xE7,                         //   Usage Maximum (231)
                (byte) 0x15, (byte) 0x00,                         //   Logical Minimum (0)
                (byte) 0x25, (byte) 0x01,                         //   Logical Maximum (1)
                (byte) 0x75, (byte) 0x01,                         //   Report Size (1)
                (byte) 0x95, (byte) 0x08,                         //   Report Count (8)
                (byte) 0x81, (byte) 0x02,                         //   Input (Data, Variable, Absolute)
                (byte) 0x95, (byte) 0x01,                         //   Report Count (1)
                (byte) 0x75, (byte) 0x08,                         //   Report Size (8)
                (byte) 0x81, (byte) 0x01,                         //   Input (Constant) reserved byte(1)
                (byte) 0x95, (byte) 0x05,                         //   Report Count (5)
                (byte) 0x75, (byte) 0x01,                         //   Report Size (1)
                (byte) 0x05, (byte) 0x08,                         //   Usage Page (Page# for LEDs)
                (byte) 0x19, (byte) 0x01,                         //   Usage Minimum (1)
                (byte) 0x29, (byte) 0x05,                         //   Usage Maximum (5)
                (byte) 0x91, (byte) 0x02,                         //   Output (Data, Variable, Absolute), Led report
                (byte) 0x95, (byte) 0x01,                         //   Report Count (1)
                (byte) 0x75, (byte) 0x03,                         //   Report Size (3)
                (byte) 0x91, (byte) 0x01,                         //   Output (Data, Variable, Absolute), Led report padding
                (byte) 0x95, (byte) 0x06,                         //   Report Count (6)
                (byte) 0x75, (byte) 0x08,                         //   Report Size (8)
                (byte) 0x15, (byte) 0x00,                         //   Logical Minimum (0)
                (byte) 0x25, (byte) 0x65,                         //   Logical Maximum (101)
                (byte) 0x05, (byte) 0x07,                         //   Usage Page (Key codes)
                (byte) 0x19, (byte) 0x00,                         //   Usage Minimum (0)
                (byte) 0x29, (byte) 0x65,                         //   Usage Maximum (101)
                (byte) 0x81, (byte) 0x00,                         //   Input (Data, Array) Key array(6 bytes)
                (byte) 0xC0                                       // End Collection (Application)

//                (byte) 0x05, (byte) 0x01,
//                (byte) 0x09, (byte) 0x02,
//                (byte) 0xa1, (byte) 0x01,
//                (byte) 0x09, (byte) 0x01,
//                (byte) 0xa1, (byte) 0x00,
//                (byte) 0x85, (byte) 0x01,
//                (byte) 0x05, (byte) 0x09,
//                (byte) 0x19, (byte) 0x01,
//                (byte) 0x29, (byte) 0x03,
//                (byte) 0x15, (byte) 0x00,
//                (byte) 0x25, (byte) 0x01,
//                (byte) 0x95, (byte) 0x03,
//                (byte) 0x75, (byte) 0x01,
//                (byte) 0x81, (byte) 0x02,
//                (byte) 0x95, (byte) 0x01,
//                (byte) 0x75, (byte) 0x05,
//                (byte) 0x81, (byte) 0x03,
//                (byte) 0x05, (byte) 0x01,
//                (byte) 0x09, (byte) 0x30,
//                (byte) 0x09, (byte) 0x31,
//                (byte) 0x09, (byte) 0x38,
//                (byte) 0x15, (byte) 0x81,
//                (byte) 0x25, (byte) 0x7f,
//                (byte) 0x75, (byte) 0x08,
//                (byte) 0x95, (byte) 0x03,
//                (byte) 0x81, (byte) 0x06,
//                (byte) 0xc0, (byte) 0xc0,
//                (byte) 0x05, (byte) 0x01,
//                (byte) 0x09, (byte) 0x06,
//                (byte) 0xa1, (byte) 0x01,
//                (byte) 0x85, (byte) 0x02,
//                (byte) 0x05, (byte) 0x07,
//                (byte) 0x19, (byte) 0xE0,
//                (byte) 0x29, (byte) 0xE7,
//                (byte) 0x15, (byte) 0x00,
//                (byte) 0x25, (byte) 0x01,
//                (byte) 0x75, (byte) 0x01,
//                (byte) 0x95, (byte) 0x08,
//                (byte) 0x81, (byte) 0x02,
//                (byte) 0x95, (byte) 0x01,
//                (byte) 0x75, (byte) 0x08,
//                (byte) 0x15, (byte) 0x00,
//                (byte) 0x25, (byte) 0x65,
//                (byte) 0x19, (byte) 0x00,
//                (byte) 0x29, (byte) 0x65,
//                (byte) 0x81, (byte) 0x00,
//                (byte) 0x05, (byte) 0x08,
//                (byte) 0x95, (byte) 0x05,
//                (byte) 0x75, (byte) 0x01,
//                (byte) 0x19, (byte) 0x01,
//                (byte) 0x29, (byte) 0x05,
//                (byte) 0x91, (byte) 0x02,
//                (byte) 0x95, (byte) 0x01,
//                (byte) 0x75, (byte) 0x03,
//                (byte) 0x91, (byte) 0x03,
//                (byte) 0xc0


//                (byte) 0x05,
//                (byte) 0x01,                    // USAGE_PAGE (Generic Desktop)
//                (byte) 0x09,
//                (byte) 0x02,                    // USAGE (Keyboard)
//                (byte) 0xa1,
//                (byte) 0x01,                    // COLLECTION (Application)
//                (byte) 0x09,
//                (byte) 0x01,                   //REPORT_ID (2)
//                (byte) 0x05,
//                (byte) 0x07,                    //   USAGE_PAGE (Keyboard)
//                (byte) 0x19,
//                (byte) 0xe0,                    //   USAGE_MINIMUM (Keyboard LeftControl)
//                (byte) 0x29,
//                (byte) 0xe7,                    //   USAGE_MAXIMUM (Keyboard Right GUI)
//                (byte) 0x15,
//                (byte) 0x00,                    //   LOGICAL_MINIMUM (0)
//                (byte) 0x25,
//                (byte) 0x01,                    //   LOGICAL_MAXIMUM (1)
//                (byte) 0x75,
//                (byte) 0x01,                    //   REPORT_SIZE (1)
//                (byte) 0x95,
//                (byte) 0x08,                    //   REPORT_COUNT (8)
//                (byte) 0x81,
//                (byte) 0x02,                    //   INPUT (Data,Var,Abs)
//                (byte) 0x95,
//                (byte) 0x01,                    //   REPORT_COUNT (1)
//                (byte) 0x75,
//                (byte) 0x08,                    //   REPORT_SIZE (8)
//                (byte) 0x81,
//                (byte) 0x03,                    //   INPUT (Cnst,Var,Abs)
//                (byte) 0x95,
//                (byte) 0x05,                    //   REPORT_COUNT (5)
//                (byte) 0x75,
//                (byte) 0x01,                    //   REPORT_SIZE (1)
//                (byte) 0x05,
//                (byte) 0x08,                    //   USAGE_PAGE (LEDs)
//                (byte) 0x19,
//                (byte) 0x01,                    //   USAGE_MINIMUM (Num Lock)
//                (byte) 0x29,
//                (byte) 0x05,                    //   USAGE_MAXIMUM (Kana)
//                (byte) 0x91,
//                (byte) 0x02,                    //   OUTPUT (Data,Var,Abs)
//                (byte) 0x95,
//                (byte) 0x01,                    //   REPORT_COUNT (1)
//                (byte) 0x75,
//                (byte) 0x03,                    //   REPORT_SIZE (3)
//                (byte) 0x91,
//                (byte) 0x03,                    //   OUTPUT (Cnst,Var,Abs)
//                (byte) 0x95,
//                (byte) 0x06,                    //   REPORT_COUNT (6)
//                (byte) 0x75,
//                (byte) 0x08,                    //   REPORT_SIZE (8)
//                (byte) 0x15,
//                (byte) 0x00,                    //   LOGICAL_MINIMUM (0)
//                (byte) 0x25,
//                (byte) 0xFF,                    //   LOGICAL_MAXIMUM (101)
//                (byte) 0x05,
//                (byte) 0x07,                    //   USAGE_PAGE (Keyboard)
//                (byte) 0x19,
//                (byte) 0x00,                    //   USAGE_MINIMUM (Reserved (no event indicated))
//                (byte) 0x29,
//                (byte) 0x65,                    //   USAGE_MAXIMUM (Keyboard Application)
//                (byte) 0x81,
//                (byte) 0x00,                    //   INPUT (Data,Ary,Abs)
//                (byte) 0xc0,                           // END_COLLECTION
        };
}
