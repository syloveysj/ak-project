import {
    isValid,
    parse,
    format,
    getYear,
    getMonth,
    endOfMonth,
    getHours,
    subDays,
    startOfISOWeek,
    endOfISOWeek, subWeeks, startOfMonth, subMonths, startOfYear
} from 'date-fns';

/**
 * ////////////// 日期类辅助函数 //////////////
 */

/**
 * @description 快捷日期范围
 */
export const ranges = {今天: [new Date(), new Date()], 当月: [startOfMonth(new Date()), new Date()]};
export const exRanges = {
    今天: [new Date(), new Date()],
    昨天: [subDays(new Date(), 1), subDays(new Date(), 1)],
    最近七天: [subDays(new Date(), 6), new Date()],
    本周: [startOfISOWeek(new Date()), new Date()],
    上周: [startOfISOWeek(subWeeks(new Date(), 1)), endOfISOWeek(subWeeks(new Date(), 1))],
    本月: [startOfMonth(new Date()), new Date()],
    上个月: [startOfMonth(subMonths(new Date(), 1)), endOfMonth(subMonths(new Date(), 1))],
    本年至今: [startOfYear(new Date()), new Date()]
};

/**
 * @description 检测字符串是否符合时间格式
 */
export const isValidDate = (dateStr: string) => {
    const date = parse(dateStr);
    return isValid(date);
};

/**
 * @description 时间戳转换成yyyy-mm-dd格式
 */
export const convertToDate = (date: Date) => {
    if (!date) {
        return null;
    }
    return format(date, 'YYYY-MM-DD');
};

/**
 * @description 时间戳转换成yyyy-mm-dd格式
 */
export const convertToFullDate = (date: Date, formatStr: string = 'YYYY-MM-DD HH:mm:ss') => {
    if (!date) {
        return null;
    }
    return format(date, formatStr);
};

/**
 * @description 获取年份
 */
export const convertToYear = (date: Date) => {
    if (!date) {
        return null;
    }
    return getYear(date);
};

/**
 * @description 获取月份
 */
export const convertToMonth = (date: Date) => {
    if (!date) {
        return null;
    }
    return getMonth(date) + 1;
};

/**
 * @description 获取月份
 */
export const convertToHour = (date: Date) => {
    if (!date) {
        return null;
    }
    return getHours(date);
};

/**
 * @description 时间正序排序
 */
export const sortDate = (date: any[]) => {
    date.sort((a, b) => {
        return Date.parse(a.xDate) - Date.parse(b.xDate); // 时间正序
    });
};
