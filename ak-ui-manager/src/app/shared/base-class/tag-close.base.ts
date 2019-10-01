/**
 * @description tag标签类
 */
import {SearchBase} from '@shared/base-class/search.base';

export class TagCloseBase extends SearchBase {

    /**
     * @description 类型
     */
    onTypeIdClose() {
        this.selfTypeId.next(this.selfQueryParams.typeId = null);
        this.resetPage();
    }

    /**
     * @description 状态
     */
    onStateTagClose() {
        this.selfStateId.next(this.selfQueryParams.stateId = null);
        this.resetPage();
    }

    /**
     * @description 关键字
     */
    onKeywordTagClose() {
        this.selfKeyword.next(this.selfQueryParams.keyword = null);
        this.resetPage();
    }

    /**
     * @description 来源
     */
    onSourceTagClose() {
        this.selfSourceId.next(this.selfQueryParams.sourceId = null);
        this.resetPage();
    }

    /**
     * @description 日期
     */
    onDateTagClose() {
        this.selfStartDate.next(this.selfQueryParams.startDate = null);
        this.selfEndDate.next(this.selfQueryParams.endDate = null);
        this.dateRangeTagCloseData = [];
        this.dateRange = [];
        this.resetPage();
    }

    /**
     * @description 维度
     */
    onDimensionTagClose() {
        this.selfDimensionId.next(this.selfQueryParams.dimensionId = null);
        this.resetPage();
    }
}
