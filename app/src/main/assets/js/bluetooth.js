let button_bluetooth = document.querySelector("button.OpenBluetooth");

button_bluetooth.onclick = function () {
    main();
}

async function findDevice() {
    console.log("Looking for any Bluetooth Device…");
    let device = await navigator.bluetooth.requestDevice({
        acceptAllDevices: true,
        optionalServices: ['battery_service']
    });
    console.log("Got device: " + device.name);
    return device;
}

async function connectDevice(device) {
    console.log("Connect to GATT Server…");
    let server = await device.gatt.connect();
    console.log("Connected to GATT Server.");
    return server;
}

async function sendData(server, serviceUUID, characteristicUUID, data) {
    let service = await server.getPrimaryService(serviceUUID);
    let characteristic = await service.getCharacteristic(characteristicUUID);
    await characteristic.writeValue(data);
    console.log("Data sent: " + data);
}

async function main() {
    let device = await findDevice();
    let server = await connectDevice(device);
    await sendData(server, 'battery_service', "battery_level", new Uint8Array([50]));
}

main().catch(error => {
    console.error("No " + error);
});