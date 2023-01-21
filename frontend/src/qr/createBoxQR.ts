// @ts-ignore
import QRCode from "qrcode";

export default function createBoxQR(url: string): Promise<string> {
    return QRCode.toDataURL(url);
}