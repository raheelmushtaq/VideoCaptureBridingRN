import { NativeModules } from "react-native";

const { VideoCaptureModule } = NativeModules

const captureVideo = async(onResult) => {
    const promise = await VideoCaptureModule.captureVideo()
}

export default {captureVideo};