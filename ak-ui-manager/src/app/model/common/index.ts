/**
 * @description 基础返回类型
 */
export interface BaseModel {
    id?: any;
    createTime?: any;
    updateTime?: any;
    checked?: boolean;
    currencySymbol?: string;
    childItems?: any[];
    childItemLoading?: boolean;
    childItemExpand?: boolean;
}

/**
 * @description 自定义接口返回参数[最外层]
 */
export interface CustomResponse<T> {
    code: number;
    data: T;
    messages: string[];
    success?: number;
}

/**
 * @description 分页返回参数
 */
export interface Paging<T> {
    first: boolean;
    last: boolean;
    number?: number;
    numberOfElements?: number;
    totalElements?: number; // 总共条数
    totalPages?: number;
    content?: T[];
    page?: number;
    pagesize: number; // 每一页多少条
    rows?: T[]; // 数据内容
    size: number; // 每一页多少条
    total: number; // 总条数
    footer?: T[] | any; // 底部数据
    other?: string;
}

export interface Summarizing<T, S> {
    item: T[];
    total: S[];
}

/**
 * @description 顶部导航栏返回类型
 */
export interface MenuBars extends BaseModel {
    id?: number;
    text?: string;
    state?: string;
    checked?: boolean;
    attributes?: string;
    children?: MenuBars[];
    iconCls?: string;
    pid?: string;
}

/**
 * @description 下拉选框返回类型
 */
export interface Option {
    disabled?: boolean;
    text: any;
    id: any;
    selected?: boolean;

    [key: string]: any;
}

/**
 * @description 下拉组合选框返回类型
 */
export interface GroupOption {
    key: string;
    child: Option;
}

/**
 * @description 国家返回类型
 */
export interface Country extends BaseModel {
    amazonUrl: string;
    code: string;
    name: string;
}

/**
 * @description 用户信息返回类型
 */
export interface UserInfo extends BaseModel {
    age: string;
    createdate: string;
    dingtalkUserId: string;
    email: string;
    ext1: string;
    ext2: string;
    ext3: string;
    hidden: string;
    isCharge: string;
    jobId: string;
    loginname: string;
    name: string;
    organizationId: string;
    password: string;
    passwordCodeTime: string;
    passwordScreCode: string;
    phone: string;
    roleId: number;
    roleName: string;
    sex: string;
    status: string;
    usertype: string;
    id: number;
    userId: number;
}

export interface Err {
    status: number;
    message: string;
    path?: string;
    timestamp: Date;
}


/**
 * @description 弹框操作类型。创建，修改，删除，批量删除，查看
 */
export enum ModelOperationType {
    CREATE,
    UPDATE,
    DELETE,
    BATCHLETE,
    VIEW,
}

/**
 * @description 上传文件后的返回类型
 */
export interface UploadFiles extends BaseModel {
    id?: number; // 附件主键
    uid?: number;
    containerId?: number; // ticketItem主键
    containerType?: string; //
    state?: number; // 状态
    filename?: string; // 文件名
    name?: string;
    tranFilename?: string;
    filesize?: number; // 文件大小
    contentType?: string; //
    digest?: string; //
    downloads?: string; //
    authorName?: string; //
    createdOn?: string; //
    description?: string; //
    directory?: string; //
    status?: string;
    response?: any;
    url?: string;
}

/**
 * @description 上传列表子项 fileList
 */
export interface UploadList extends BaseModel {
    uid?: string;
    name?: string;
    status?: string;
    response?: any;
    url?: string;
}

// 上传成功后的回参
export interface UploadSuccess extends BaseModel {
    attachmentId?: number;
    fileInfo?: {
        lastModified: number;
        lastModifiedDate: number;
        name: string;
        size: number;
        type: string;
        uid: string;
        webkitRelativePath: string;
    };
}

/**
 * @description 自定义列表
 */
export interface SelfColumn {
    label: string;
    value: string;
    checked?: boolean;
    disabled?: boolean;
    type?: any;
}
