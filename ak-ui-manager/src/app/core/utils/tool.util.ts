import {Option, SelfColumn} from '@model/common';
import {NzTreeNodeOptions} from 'ng-zorro-antd';
import {differenceInCalendarDays} from 'date-fns';

/**
 * ////////////// 其他类辅助函数 //////////////
 */

/**
 *  @description 多级店铺递归查询
 * @param data 数组对象
 */
export function translateGroupCombobox(data: Option[]) {
    const m = new Map();
    data.forEach(
        (item) => {
            if (m.has(item.group)) {
                const arr = m.get(item.group);
                arr.push({id: item.id, text: item.text});
            } else {
                const arr = [];
                arr.push({id: item.id, text: item.text});
                m.set(item.group, arr);
            }
        });
    let markets = [];
    m.forEach((value: any, key: any) => {
        markets = [...markets, {key: key, child: value}];
    });
    return markets;
}

/**
 * @description 精确匹配自定义字符串。匹配返回字符串，否则返回undefined
 * @param columns 数据数组
 * @param str 需要匹配数据
 */
export function exactStr(columns: string[], str: string) {
    return columns && columns.find(item => item === str);
}

/**
 * @description 减少昂贵的dom操作，列表for循环使用，大幅度提升性能
 * -适用范围：当有新增，删除，修改操作的时候，请使用当前方法
 * https://netbasal.com/angular-2-improve-performance-with-trackby-cc147b5104e5
 * @param index 索引
 * @param item 当前对象
 */
export function trackById(index: number, item: any) {
    return item.id;
}

/**
 * @description 对象转字符串，导出处理
 * @param globalParams
 */
export function queryParamsMap(globalParams: object) {
    let strs = '';
    for (const attr in globalParams) {
        strs += (attr + '=' + globalParams[attr] + '&');
    }
    return strs.slice(0, -1);
}

// 工具函数，将数组转换为字符串
export function translateArrToString(arr: Array<string>, symbol = '') {
    return arr && arr.length && arr.join(symbol) || '';
}

/**
 * @description 富文本框内容需要转码(保存时转码,查看时解码)
 * 用正则表达式实现html转码
 */
export function htmlEncodeByRegExp(str) {
    let s = '';
    if (str.length === 0) {
        return '';
    }
    s = str.replace(/&/g, '&amp;');
    s = s.replace(/</g, '&lt;');
    s = s.replace(/>/g, '&gt;');
    s = s.replace(/\'/g, '&#39;');
    s = s.replace(/\"/g, '&quot;');
    s = s.replace(/\n"/g, '');
    s = s.replace(/\r"/g, '');
    s = s.replace(/\+/g, '%2B');
    return s;
}

/**
 * @description 映射转换，转换成 [object, object2] => {id： object, id2： object2} 形式;
 * @param options: 需要转换的数组
 * @param key 键值对的键
 */
export function transFormMap(options: any[], key = 'id') {
    const obj: any = {};
    if (options && options.length) {
        for (const item of options) {
            obj[item[key]] = item;
        }
        return obj;
    } else {
        return {};
    }
}

export function transformObjToMap(obj: any, id = 'id') {
    const map: any = {};
    for (let key in obj) {
        if (obj.hasOwnProperty(key)) {
            map[obj[key][id]] = obj[key];
        }
    }
    return map;
}

export function toMap(datas: any[], id: string) {
    const obj: any = {};
    if (datas && datas.length) {
        for (const item of datas) {
            obj[item[id]] = item;
        }
        return obj;
    } else {
        return {};
    }
}

/**
 * @description 富文本框内容需要转码(保存时转码,查看时解码)
 * 用正则表达式实现html解码
 */
export function htmlDecodeByRegExp(str) {
    let s = '';
    if (str.length === 0) {
        return '';
    }
    s = str.replace(/&amp;/g, '&');
    s = s.replace(/&lt;/g, '<');
    s = s.replace(/&gt;/g, '>');
    s = s.replace(/&nbsp;/g, ' ');
    s = s.replace(/&#39;/g, '\'');
    s = s.replace(/&quot;/g, '\"');
    return s;
}

export function isBlank(str: any) {
    return str === undefined ||  str === null || str === '';
}

export function range(start: number, end: number): number[] {
    const result: number[] = [];
    for (let i = start; i < end; i++) {
        result.push(i);
    }
    return result;
}

export function unique(arr: any[], id: any = 'id') {
    const res = new Map();
    return arr.filter((a) => !res.has(a[id]) && res.set(a[id], 1))
}

export function disabledDateTime(current: Date) {
    return {
        nzDisabledHours: range(0, current.getHours()),
        nzDisabledMinutes: range(0, current.getMinutes()),
        nzDisabledSeconds: range(0, current.getSeconds()),
    }
}

export function disabledStartDate(endDate: Date | string = new Date()) {
    return (current: Date): boolean => {
        if (endDate === null) {
            return false;
        }
        if (typeof endDate === 'string') {
            if (endDate === '') {
                return false;
            }
            endDate = new Date(endDate);
        }
        return differenceInCalendarDays(current, endDate) > 0;
    };
}

export function disabledEndDate(startDate: Date | string = new Date()) {
    return (current: Date): boolean => {
        if (typeof startDate === 'string') {
            startDate = new Date(startDate);
        }
        return differenceInCalendarDays(current, startDate) < 0;
    };
}

export function disabledIntervalDate(date: Date, interval: number) {
    return (current: Date): boolean => {
        if (isBlank(date)) return true;
        if (typeof date === 'string') {
            date = new Date(date);
        }
        return Math.abs(differenceInCalendarDays(current, date)) > interval;
    };
}

export function serialize(obj) {
    const str = [];
    for(let p in obj)
        if (obj.hasOwnProperty(p)) {
            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
        }
    return str.join("&");
}

/**
 * @description 解析数据流，比如读取后端出过来的数据流转换成PDF直接在页面上面显示
 */
export function createPDFFromBlob(blob: Blob, name: string = 'file.pdf') {
    const newBlob = new Blob([blob], {type: 'application/pdf'});
    // 兼容IE
    if (window.navigator && window.navigator.msSaveOrOpenBlob) {
        window.navigator.msSaveOrOpenBlob(newBlob);
        return;
    }
    const data = window.URL.createObjectURL(newBlob);
    const link = document.createElement('a');
    link.href = data;
    link.download = name;
    link.click();
    // 兼容火狐
    setTimeout(() => {
        window.URL.revokeObjectURL(data);
    }, 100);
}

/**
 * 递归打平NzTreeNodeOptions数据结构，打平成一级
 * @param nodes 节点数据
 * @example
 */

export function nzTreeNodeOptionsFlat(nodes: NzTreeNodeOptions[]): NzTreeNodeOptions[] {
    let flatNodes: NzTreeNodeOptions[] = [];
    for (const node of nodes) {
        if (!node.isLeaf) {
            flatNodes = [...flatNodes, ...(nzTreeNodeOptionsFlat(node.children)), node] as NzTreeNodeOptions[];
        } else {
            flatNodes = [...flatNodes, node] as NzTreeNodeOptions[];
        }
    }
    /*if (turnToMap) {
        const obj: any = {};
        if (flatNodes && flatNodes.length) {
            for (const item of flatNodes) {
                flatNodes[item.key] = item;
            }
            return obj;
        } else {
            return {};
        }
    }*/
    return flatNodes;
}

// checkbox 匹配勾选中的元素
export function getCheckedElements(source: SelfColumn[], selectedElement: string[]) {
    source.map(item => item.checked = false);
    for (const item of selectedElement) {
        for (const cItem of source) {
            if (cItem.value === item) {
                cItem.checked = true;
                break;
            }
        }
    }
    return source;
}

// 遍历元素，给元素添加disable=true
export function getDisabledElements(source: SelfColumn[], selectedElement: string[]) {
    source.map(item => item.disabled = false);
    for (const item of selectedElement) {
        for (const cItem of source) {
            if (cItem.value === item) {
                cItem.disabled = true;
                break;
            }
        }
    }
    return source;
}

// 通过js的内置对象JSON来进行数组对象的深拷贝
export function deepClone<T>(obj: T): T {
    const cobj = JSON.stringify(obj);
    const objClone = JSON.parse(cobj);
    return objClone;
}

// 二维数组打平成一维数组
export function flatArr(arr: any[]): Array<any> {
    return arr.reduce((curr, prev) => {
        return [...curr, ...prev];
    }, []);
}
