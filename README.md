# IoMT Health Center

[![Run diKTat](https://github.com/IoMT-LVK/iomt-android/actions/workflows/diktat.yml/badge.svg)](https://github.com/IoMT-LVK/iomt-android/actions/workflows/diktat.yml)
![Lines of code](https://img.shields.io/tokei/lines/github/IoMT-LVK/iomt-android)
[![Hits-of-Code](https://hitsofcode.com/github/IoMT-LVK/iomt-android?branch=main)](https://hitsofcode.com/github/IoMT-LVK/iomt-android/view?branch=main)


### What's inside
Application is used to read data from BLE devices and send it to [server](https://github.com/IoMT-LVK/iomt_backend)

### Requirements:
 * Android 14
 * BLE and BC support

### Features
 * Credential saving
 * Dynamic theme based on `MaterialYou`
 * Caching data on device
 * Plots based on cached data
 * `MQTT` is used for data synchronization with server
 * Auto cleanup each 24 hours

### Some screenshots
![Login View](screenshots/login_view.jpg)
![Config Selection](screenshots/config_selection.jpg)
![Device View](screenshots/device_view.jpg)
![Cardio View](screenshots/cardio.jpg)
