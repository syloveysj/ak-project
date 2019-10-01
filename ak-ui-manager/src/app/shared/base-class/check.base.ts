/**
 * @description 基类组件，抽取公共的属性和方法
 */
import {PropertiesBase} from '@shared/base-class/properties.base';
import {SelfColumn} from '@model/common';

export class CheckBase extends PropertiesBase {

    // 多选操作全家桶变量
    allChecked = false;
    disabledButton = true;
    checkedNumber = 0;
    displayData: any[] = [];
    indeterminate = false;
    allPageChecked = false;

    /**
     * 获取选中的ids; 字符串形式
     */
    get checkedIds() {
        return this.displayData.filter(item => item.checked).map(item => item.id + '').join(',');
    }

    /**
     * @description 数组形式
     */
    get checkIdsArr() {
        return this.displayData.filter(item => item.checked).map(item => String(item.id));
    }

    /**
     * @description 数组-number形式
     */
    get checkIdsArrNumber() {
        return this.displayData.filter(item => item.checked).map(item => Number(item.id));
    }

    /**
     * @description 获取选中的item对象
     */
    get checkedItems() {
        return this.displayData.filter(item => item.checked);
    }

    /**
     * @description 以下五个方法为多选操作全家桶
     * resetCheckStatus()
     * refreshStatus()
     * checkAll()
     * checkAllPage()
     * currentPageDataChange()
     */
    resetCheckStatus() {
        this.allChecked = false;
        this.disabledButton = true;
        this.checkedNumber = 0;
        this.displayData = [];
        this.indeterminate = false;
    }

    refreshStatus(): void {
        const allChecked = this.displayData.length ? this.displayData.every(value => value.checked === true) : false;
        const allUnChecked = this.displayData.every(value => !value.checked);
        this.allChecked = allChecked;
        this.indeterminate = (!allChecked) && (!allUnChecked);
        this.disabledButton = !this.displayData.some(value => value.checked);
        this.checkedNumber = this.displayData.filter(value => value.checked).length;
        if (!allChecked) {
            this.allPageChecked = false;
        }
    }

    checkAll(value: boolean): void {
        this.displayData.forEach(data => data.checked = value);
        this.refreshStatus();
    }

    checkAllPage(checked: boolean) {
        this.allPageChecked = checked;
        if (this.allPageChecked) {
            // 全选中所有页面的数据
            this.checkAll(true);
        } else {
            // 所有页面数据都不选中
            this.checkAll(false);
        }
    }

    checkColumns(allColumns: SelfColumn[], selectedColumns: string[]) {
        for (const item of allColumns) {
            for (const s of selectedColumns) {
                if (s === item.value) {
                    item.checked = true;
                    break;
                } else {
                    item.checked = false;
                }
            }
        }
    }

    currentPageDataChange($event: any[]): void {
        this.displayData = $event;
        this.checkAllPage(this.allPageChecked);
    }

}
