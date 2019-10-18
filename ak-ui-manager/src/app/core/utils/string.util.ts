export function isEmpty(str: string): boolean {
    return str === undefined ||  str === null || str === '';
}

export function isNotEmpty(str: string): boolean {
    return !isEmpty(str);
}
