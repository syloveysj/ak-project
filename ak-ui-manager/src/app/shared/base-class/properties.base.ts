import {defaultImg, modalTitle, nzPageSizeOptions, nzResWidth} from '@core/utils/constant.util';
import {deepClone, exactStr, queryParamsMap, trackById} from '@core/utils/tool.util';
import {convertToDate, exRanges} from '@core/utils/date.util';
import {Observable, Subject} from 'rxjs';
import {SelfColumn} from '@model/common';
import {subDays} from 'date-fns';

export class PropertiesBase {
    nzResWidth = nzResWidth; // 响应式宽度
    defaultImg = defaultImg; // 默认图片
    nzPageSizeOptions = nzPageSizeOptions; // 每页可选展示记录数
    modalTitle = modalTitle; // 弹框操作类型(增加，修改，删除，批量删除，查看)
    trackById = trackById; // 列表for循环减少dom重新渲染，提升性能操作
    exactStr = exactStr; // 自定义列函数，精确匹配字符串
    queryParamsMap = queryParamsMap; // 查询参数处理函数
    ranges = exRanges;
    exRanges = deepClone(exRanges);
    isShowAdviceSearch = false; // 是否显示高级搜索区块
    calcWidth: string;
    data: any; // 数据源
    loading = true; // 加载flag
    scrollY: any;  // tabY轴-自适应
    buttonAuth = {};    // 按钮权限集合控制
    dateRangeTagCloseData = []; // 日期范围标签数据
    dateRange = [convertToDate(subDays(new Date(), 14)), convertToDate(new Date())]; // 日期范围默认是15天前到现在
    globalParams = {}; // 全局参数, 捕获过滤条件，常用于导出操作

    // 获取底部统计信息
    get footerStatistics() {
        return this.data && this.data.footer && this.data.footer.length && this.data.footer[0];
    }

    // 自定义列相关变量
    columns = [];
    COLUMNS: SelfColumn[] = [];
    defaultColumns = this.COLUMNS.map(item => item.value);

    // 应用分类
    applicationTypes$: Observable<any[]>;
    // 服务应用
    services$: Observable<any[]>;

    // 组件内部查询参数
    selfQueryParams: { [key: string]: any } = {
        dimensionId: null,
        dynamicKey: null,
        keyword: null,
        stateId: null,
        serverId: null,
        typeId: null,
        startDate: null,
        endDate: null,
        disposition: null,
        statisticsYear: null,
        statisticsMonth: null,
        sort: 'recordDate',
        order: 'descend',
        page: 1, // 默认当前页
        pagesize: 20, // 每页条数
        dateType: null,
        state: null,
    };
    queryParams: any = {}; // 路由跳转参数

    // 定义初始值的变量
    selfInitialState = {
        dateTypeId: null,
        dimensionId: null,
        dynamicKey: null,
        keyword: null,
        stateId: null,
        typeId: null,
        dateRange: null,
        dateRangeTagCloseData: null,
        startDate: null,
        endDate: null,
        disposition: null,
        statisticsYear: null,
        statisticsMonth: null,
        sort: 'recordDate',
        order: 'descend',
        page: 1, // 默认当前页
        pagesize: 20, // 每页条数
        dateType: null,
        statisticsStartDate: null,
        statisticsEndDate: null,
        state: null,
    };

    // 公共的一些Subject
    selfDimensionId = new Subject<string>(); // 查询维度
    selfDynamicKey = new Subject<string>(); // 动态关键字
    selfKeyword = new Subject<string>(); // 搜索关键字
    selfSourceId = new Subject<string>(); // 来源id
    selfStateId = new Subject<string | number>(); // 状态
    selfTypeId = new Subject<string | number>(); // 类型

    selfStartDate = new Subject<string>(); // 时间选择-开始时间
    selfEndDate = new Subject<string>(); // 时间选择-结束时间

    selfStatisticsYear = new Subject<number>(); // 时间选择-年
    selfStatisticsMonth = new Subject<number>(); // 时间选择-月

    selfSort = new Subject<string>();
    selfOrder = new Subject<'descend' | 'ascend' | null>();
    selfPage = new Subject<number>();
    selfPageSize = new Subject<number>();
    selfDateType = new Subject<string | number>();

    // 日期控件国际化
    constructor() {
    }

}
