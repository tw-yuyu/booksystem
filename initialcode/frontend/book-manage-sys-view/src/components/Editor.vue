<template>
    <div>
        <Toolbar style="border-bottom: 1px solid #eae8e8;" :editor="editor" :defaultConfig="toolbarConfig"
            :mode="mode" />
        <Editor :style="{ height: height, overflowY: 'hidden' }" v-model="content" :defaultConfig="editorConfig"
            :mode="mode" @onCreated="onCreated" />
    </div>
</template>
<script>
import Vue from 'vue'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
export default Vue.extend({
    components: { Editor, Toolbar },
    props: {
        receiveContent: {
            type: String,
            default: '',
            required: true
        },
        height: {
            type: String,
            default: 'calc(100vh - 100px)'
        }
    },
    data() {
        return {
            editor: null,
            content: '<p>創作內容</p>',
            toolbarConfig: {},
            editorConfig: {
                placeholder: '請輸入題目...',
                MENU_CONF: {
                    uploadImage: {
                        server: '/api/book-manage-sys-api/v1.0/file/upload',
                        fieldName: 'file',
                        maxFileSize: 10 * 1024 * 1024,
                        maxNumberOfFiles: 10,
                        // allowedFileTypes: ['image/*'],
                        metaWithUrl: false,
                        withCredentials: true,
                        timeout: 10 * 1000,
                        headers: {
                            'token': sessionStorage.getItem('token')
                        },
                        customInsert(res, insertFn) {
                            insertFn(res.data, res.data, res.data);
                        },
                    },
                    uploadVideo: {
                        server: '/api/book-manage-sys-api/v1.0/file/upload',
                        fieldName: 'file',
                        maxFileSize: 100 * 1024 * 1024,
                        headers: {
                            'token': sessionStorage.getItem('token')
                        },
                        timeout: 60 * 1000,
                        customInsert(res, insertFn) {
                            insertFn(res.data, res.data);
                        },
                    }
                }
            },
            mode: 'default',
        }
    },
    methods: {
        onCreated(editor) {
            this.editor = Object.seal(editor);
            this.toolbarConfig.excludeKeys = ['group-video','insertLink','fullScreen','emotion','insertTable'];
        },
    },
    watch: {
        receiveContent: {
            handler(v1, v2) {
                this.content = v1;
            },
            deep: true, // 啟用深度監聽  
            immediate: true
        },
        content(newVal, oldVal) {
            this.$emit('on-receive', newVal);
        },
    },
    beforeDestroy() {
        const editor = this.editor;
        if (editor == null) return;
        editor.destroy();
    }
})
</script>
<style src="@wangeditor/editor/dist/css/style.css"></style>
<style scoped>
.line-number {
    display: block;
    margin-right: 10px;
    /* 以下樣式確保行號不被選中或複製 */
    pointer-events: none;
    user-select: none;
    -webkit-user-select: none;
    color: #999;
    /* 行號顏色，可自訂義 */
    text-align: right;
    /* 行號對齊方式 */
}
</style>
