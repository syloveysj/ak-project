import {HttpParameterCodec} from "@angular/common/http";

export class HttpQueryEncoderUtil implements HttpParameterCodec {
    encodeKey(key: string): string {
        return this.encode(key);
    }

    encodeValue(value: string): string {
        return this.encode(value);
    }

    decodeKey(key: string): string {
        return this.decode(key);
    }

    decodeValue(value: string): string {
        return this.decode(value);
    }

    private encode(v: string): string {
        return encodeURIComponent(v);
    }

    private decode(v: string): string {
        return decodeURIComponent(v);
    }

}
