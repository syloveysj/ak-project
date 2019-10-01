import {Injectable} from '@angular/core';
import {Location} from '@angular/common';
import {MenuBars} from '@model/common';

@Injectable({
    providedIn: 'root'
})
export class ToolService {

    buttons: MenuBars[];

    public constructor(private location: Location) {
    }

    // ========================== [属性集合] ==========================//

    /**
     * @description 分页选择器改变的条数
     */
    nzPageSizeOptions = [10, 20, 50, 100];


    /**
     * @description 响应式布局间隔范式
     * [nzXs]    <576px 响应式栅格，可为栅格数或一个包含其他属性的对象    number丨object    -
     * [nzSm]    ≥576px 响应式栅格，可为栅格数或一个包含其他属性的对象    number丨object    -
     * [nzMd]    ≥768px 响应式栅格，可为栅格数或一个包含其他属性的对象    number丨object    -
     * [nzLg]    ≥992px 响应式栅格，可为栅格数或一个包含其他属性的对象    number丨object    -
     * [nzXl]    ≥1200px 响应式栅格，可为栅格数或一个包含其他属性的对象    number丨object    -
     * [nzXXl]    ≥1600px 响应式栅格，可为栅格数或一个包含其他属性的对象    number丨object    -
     * [nzXXl] = "{span: 6, offset: 2}" 占6格，向右偏移2格;
     */
    nzGutter = {xs: 8, sm: 8, md: 8, lg: 8, xl: 8, xxl: 8};
    nzGutter2 = {xs: 8, sm: 16, md: 24, lg: 24, xl: 24, xxl: 24};

    // ========================== [方法集合] ==========================//


    /**
     * @description 返回到上一层
     */
    public goBack() {
        this.location.back();
    }

    /**
     * @description 获取页面按钮权限
     * @param buttonAuth 自定义按钮权限集合
     * @param data 匹配的按钮信息对象
     * @example:
     *  data = {
     *
     *  }
     */
    public getButtonsAuth(buttonAuth: any, data: any) {
        for (const item in data) {
            buttonAuth[item] = this._buttonAuthValidate(data[item].path);
        }
    }

    /**
     * @description 按钮权限验证
     * @param path 路径
     */
    private _buttonAuthValidate(path: string) {
        const thirdMenus = this.buttons || [];
        return !!thirdMenus.find(item => item.attributes === path);
    }

}
