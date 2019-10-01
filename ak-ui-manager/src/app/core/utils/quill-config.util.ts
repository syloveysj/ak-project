/**
 * @description quill富文本编辑器配置文件
 */
// quill编辑器的字体
// const fonts = ['SimSun', 'SimHei', 'Microsoft-YaHei', 'KaiTi', 'FangSong', 'Arial', 'Times-New-Roman', 'sans-serif'];
// const Font = Quill.import('formats/font');
// Font.whitelist = fonts; // 将字体加入到白名单
// Quill.register(Font, true);

export const quillConfig = {
    toolbar: [
        ['bold', 'italic', 'underline', 'strike'],        // toggled buttons
        ['blockquote', 'code-block'],
        // [{ 'header': 1 }, { 'header': 2 }],               // custom button values
        [{list: 'ordered'}, {list: 'bullet'}],
        [{script: 'sub'}, {script: 'super'}],      // superscript/subscript
        [{indent: '-1'}, {indent: '+1'}],          // outdent/indent
        [{direction: 'rtl'}],                         // text direction
        // [{ 'size': ['small', false, 'large', 'huge'] }],  // custom dropdown
        [{header: [1, 2, 3, 4, 5, 6, false]}],
        [{color: []}, {background: []}],          // dropdown with defaults from theme
        [{align: []}],
        ['clean'],                                         // remove formatting button
        // [{'font': fonts}],
        ['link']
    ]
};
