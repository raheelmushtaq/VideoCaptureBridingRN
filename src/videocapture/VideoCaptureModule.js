import { NativeModules } from "react-native";

const { VideoCaptureModule } = NativeModules

const captureVideo = () => {
    console.log(VideoCaptureModule)
    VideoCaptureModule.captureVideo()
}

export default {captureVideo};