import {CustomResponse} from "@model/common";

export function isSuccess(result: CustomResponse<any>): boolean {
    if(result.code === 200) return true;
    return false;
}
