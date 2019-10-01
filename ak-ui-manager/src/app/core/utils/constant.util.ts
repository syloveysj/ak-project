/**
 * ////////////// 常量 //////////////
 */
/**
 * @description 分页选择器改变的条数
 */
export const nzPageSizeOptions = [10, 20, 50, 100];
/**
 * @description 默认的防止抖动时间
 */
export const defaultDebounceTime = 500;

/**
 * @description iconfontUrl图标字体地址
 */
export const iconfontUrl = 'https://at.alicdn.com/t/font_994509_nfjkhq6cja.js';

/**
 * @description 没有图片的时候展示的默认图片
 */
export const defaultImg = '../../assets/images/default-img.png';

/**
 * @description 一般的响应式宽度
 */
export const nzResWidth = {nzXs: 24, nzSm: 24, nzMd: 4, nzLg: 3, nzXl: 3, nzXXl: 2};

/**
 * @description 详情字段展示响应式宽度
 */
export const nzDetailResWidth = {nzXs: 24, nzSm: 24, nzMd: 8, nzLg: 8, nzXl: 8, nzXXl: 8};

/**
 * @description 验证url地址
 */
export const urlRegExp = /(http|ftp|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&amp;:/~\+#]*[\w\-\@?^=%&amp;/~\+#])?/;

export const activityStatusFormat = ['无兴趣', '有兴趣', '已下单', '已收货', '已完成'];
export const registeredFormat: string[] = ['否', '是']; // 是否注册
export const modalTitle: string[] = ['添加', '修改', '删除', '批量删除', '查看'];

/**
 * @description 默认全部的option
 */
export const defaultAllOption = {text: '全部', id: null};

/**
 * @description 高级搜索-默认显示子项的个数
 */
export const defaultShowItemLength = 15;

export enum ResponseCode {
    SUCCESS = 0,
    ERROR = 1
}
