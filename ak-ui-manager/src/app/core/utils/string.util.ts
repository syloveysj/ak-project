export function isEmpty(str: string): boolean {
    if(str === null) return true;
    if(str.length === 0) return true;
    return false;
}

export function isNotEmpty(str: string): boolean {
    return !isEmpty(str);
}