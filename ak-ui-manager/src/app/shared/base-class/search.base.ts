import {startWith} from 'rxjs/operators';
import {convertToDate, convertToFullDate, convertToMonth, convertToYear} from '@core/utils/date.util';
import {ModalBase} from '@shared/base-class/modal.base';

/**
 * @description 过滤查询类的基类
 */
export class SearchBase extends ModalBase {
    /**
     * @description 获取筛选参数流
     */
    getGeneralObservables() {
        return {
            typeId$: this.selfTypeId.asObservable().pipe(startWith(this.selfQueryParams.typeId)),
            stateId$: this.selfStateId.asObservable().pipe(startWith(this.selfQueryParams.stateId)),
            dynamicKey$: this.selfDynamicKey.asObservable().pipe(startWith(this.selfQueryParams.dynamicKey)),
            keyword$: this.selfKeyword.asObservable().pipe(startWith(this.selfQueryParams.keyword)),
            dimensionId$: this.selfDimensionId.asObservable().pipe(startWith(this.selfQueryParams.dimensionId)),
            page$: this.selfPage.asObservable().pipe(startWith(this.selfQueryParams.page)),
            pageSize$: this.selfPageSize.asObservable().pipe(startWith(this.selfQueryParams.pagesize)),
            sort$: this.selfSort.asObservable().pipe(startWith(this.selfQueryParams.sort)),
            order$: this.selfOrder.asObservable().pipe(startWith(this.selfQueryParams.order)),
            startDate$: this.selfStartDate.asObservable().pipe(startWith(this.selfQueryParams.startDate)),
            endDate$: this.selfEndDate.asObservable().pipe(startWith(this.selfQueryParams.endDate)),
            statisticsYear$: this.selfStatisticsYear.asObservable().pipe(startWith(this.selfQueryParams.statisticsYear)),
            statisticsMonth$: this.selfStatisticsMonth.asObservable().pipe(startWith(this.selfQueryParams.statisticsMonth)),
            dateType$: this.selfDateType.asObservable().pipe(startWith(this.selfQueryParams.dateType)),
            sourceId$: this.selfSourceId.asObservable().pipe(startWith(this.selfQueryParams.sourceId)),
        };
    }

    ///////////////// select相关的方法 /////////////////////

    /**
     * @description 来源
     */
    sourceChange() {
        this.selfSourceId.next(this.selfQueryParams.sourceId);
        this.resetPage();
    }

    /**
     * @description 类型
     */
    typeChange() {
        this.selfTypeId.next(this.selfQueryParams.typeId);
        this.resetPage();
    }

    /**
     * @description 状态
     */
    stateChange() {
        this.selfStateId.next(this.selfQueryParams.stateId);
        this.resetPage();
    }

    /**
     * @description 组合搜索类别
     * @param value 筛选查询的类别
     */
    labelChange(value: string) {
        this.selfDynamicKey.next(this.selfQueryParams.dynamicKey = value);
        this.resetPage();
    }

    /**
     * @description 关键字
     */
    keywordChange() {
        this.selfKeyword.next(this.selfQueryParams.keyword);
        this.resetPage();
    }

    dynamicKeyChange() {
        this.selfDynamicKey.next(this.selfQueryParams.dynamicKey);
        this.resetPage();
    }

    /**
     * @description table字段排序处理
     * @param sort 排序规则值 【descend-降序, ascend-升序, null-默认】
     */
    sortChange(sort: { key: string, value: 'descend' | 'ascend' | null }): void {
        this.selfSort.next(this.selfQueryParams.sort = sort.key);
        this.selfOrder.next(this.selfQueryParams.order = sort.value);
        this.resetPage();
    }

    /**
     * @description 分页跳转
     * @param value 跳转的页数
     */
    pageChange(value: number) {
        this.selfPage.next(this.selfQueryParams.page = value);
    }

    /**
     * @description 改变每页条数
     * @param value 每页显示的条数
     */
    pageSizeChange(value: number) {
        this.selfPageSize.next(this.selfQueryParams.pagesize = value);
        this.resetPage();
    }

    /**
     * @description 日期范围变更
     * @param result [开始时间，结束时间]
     */
    onTimeChange(result: Date): void {
        this.selfStartDate.next(this.selfQueryParams.startDate = convertToDate(result[0]));
        this.selfEndDate.next(this.selfQueryParams.endDate = convertToDate(result[1]));
        this.resetPage();
    }

    /**
     * @description 日期范围变更
     * @param result [年，月]
     */
    onStatisticsTimeChange(result: Date): void {
        this.selfStatisticsYear.next(this.selfQueryParams.statisticsYear = convertToYear(result));
        this.selfStatisticsMonth.next(this.selfQueryParams.statisticsMonth = convertToMonth(result));
        this.resetPage();
    }

    /**
     * @description 操作是否显示高级搜索区块
     */
    advancedSearchClick(state: boolean) {
        this.isShowAdviceSearch = state;
    }

    resetPage() {
        this.selfPage.next(this.selfQueryParams.page = 1);
    }

    /**
     * 日期选择
     */
    dateChange(value: any) {
        this.selfDateType.next(value);
        this.selfQueryParams.page = 1;
        this.selfPage.next(this.selfQueryParams.page);
    }

    startDateChange(startDate: Date, withTime: boolean = false) {
        this.selfStartDate.next(this.selfQueryParams.startDate = withTime ? convertToFullDate(startDate) : convertToDate(startDate));
        this.resetPage();
    }

    endDateChange(endDate: Date, withTime: boolean = false) {
        this.selfEndDate.next(this.selfQueryParams.endDate = withTime ? convertToFullDate(endDate) : convertToDate(endDate));
        this.resetPage();
    }

}
