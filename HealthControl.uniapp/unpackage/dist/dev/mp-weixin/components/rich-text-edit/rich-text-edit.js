"use strict";
const common_vendor = require("../../common/vendor.js");
const store_index = require("../../store/index.js");
const utils_comm = require("../../utils/comm.js");
const utils_http = require("../../utils/http.js");
const _sfc_main = {
  __name: "rich-text-edit",
  props: {
    modelValue: {
      type: String,
      default: ""
    },
    readOnly: {
      type: Boolean,
      default: false
    }
  },
  emits: ["update:modelValue", "contentChange"],
  setup(__props, { expose: __expose, emit: __emit }) {
    store_index.useCommonStore();
    const props = __props;
    const emit = __emit;
    const formats = common_vendor.ref({});
    const content = common_vendor.ref("");
    const editorCtx = common_vendor.ref(null);
    const onEditorReady = () => {
      common_vendor.index.createSelectorQuery().select("#editor").context((res) => {
        editorCtx.value = res.context;
        if (props.modelValue) {
          setTimeout(() => {
            setContent(props.modelValue);
          }, 1500);
        }
      }).exec();
    };
    const undo = () => {
      editorCtx.value.undo();
    };
    const redo = () => {
      editorCtx.value.redo();
    };
    const format = (e) => {
      let { name, value } = e.target.dataset;
      if (!name)
        return;
      editorCtx.value.format(name, value);
    };
    const onStatusChange = (e) => {
      formats.value = e.detail;
    };
    const insertDivider = () => {
      editorCtx.value.insertDivider({
        success: function() {
        }
      });
    };
    const clear = () => {
      editorCtx.value.clear({
        success: function(res) {
        }
      });
    };
    const removeFormat = () => {
      editorCtx.value.removeFormat();
    };
    const insertDate = () => {
      const date = /* @__PURE__ */ new Date();
      const formatDate = `${date.getFullYear()}/${date.getMonth() + 1}/${date.getDate()}`;
      editorCtx.value.insertText({
        text: formatDate
      });
    };
    const insertImage = async () => {
      try {
        const { tempFilePaths, tempFiles } = await utils_comm.ChooseImageAsync(1);
        if (tempFilePaths && tempFilePaths.length > 0) {
          const { Data } = await utils_http.Upload(tempFilePaths[0]);
          editorCtx.value.insertImage({
            src: Data[0].Url,
            alt: "图像",
            success: function() {
            }
          });
        }
      } catch (error) {
        common_vendor.index.__f__("error", "at components/rich-text-edit/rich-text-edit.vue:191", "插入图片失败:", error);
      }
    };
    const getContent = async () => {
      return new Promise((resolve, reject) => {
        editorCtx.value.getContents({
          success: async ({ html, text, delta }) => {
            content.value = html;
            emit("update:modelValue", html);
            emit("contentChange", { html, text, delta });
            resolve(html);
          },
          fail: (err) => {
            reject(err);
          }
        });
      });
    };
    const setContent = (text) => {
      if (!editorCtx.value) {
        setTimeout(() => {
          setContent(text);
        }, 1500);
        return;
      }
      editorCtx.value.setContents({
        html: text
      });
    };
    __expose({
      getContent,
      setContent,
      clear,
      undo,
      redo
    });
    return (_ctx, _cache) => {
      return {
        a: common_vendor.n(formats.value.bold ? "ql-active" : ""),
        b: common_vendor.n(formats.value.italic ? "ql-active" : ""),
        c: common_vendor.n(formats.value.underline ? "ql-active" : ""),
        d: common_vendor.n(formats.value.strike ? "ql-active" : ""),
        e: common_vendor.n(formats.value.align === "left" ? "ql-active" : ""),
        f: common_vendor.n(formats.value.align === "center" ? "ql-active" : ""),
        g: common_vendor.n(formats.value.align === "right" ? "ql-active" : ""),
        h: common_vendor.n(formats.value.align === "justify" ? "ql-active" : ""),
        i: common_vendor.n(formats.value.lineHeight ? "ql-active" : ""),
        j: common_vendor.n(formats.value.letterSpacing ? "ql-active" : ""),
        k: common_vendor.n(formats.value.marginTop ? "ql-active" : ""),
        l: common_vendor.n(formats.value.previewarginBottom ? "ql-active" : ""),
        m: common_vendor.o(removeFormat),
        n: common_vendor.n(formats.value.fontFamily ? "ql-active" : ""),
        o: common_vendor.n(formats.value.fontSize === "24px" ? "ql-active" : ""),
        p: common_vendor.n(formats.value.color === "#0000ff" ? "ql-active" : ""),
        q: common_vendor.n(formats.value.backgroundColor === "#00ff00" ? "ql-active" : ""),
        r: common_vendor.o(insertDate),
        s: common_vendor.n(formats.value.list === "ordered" ? "ql-active" : ""),
        t: common_vendor.n(formats.value.list === "bullet" ? "ql-active" : ""),
        v: common_vendor.o(undo),
        w: common_vendor.o(redo),
        x: common_vendor.o(insertDivider),
        y: common_vendor.o(insertImage),
        z: common_vendor.n(formats.value.header === 1 ? "ql-active" : ""),
        A: common_vendor.n(formats.value.script === "sub" ? "ql-active" : ""),
        B: common_vendor.n(formats.value.script === "super" ? "ql-active" : ""),
        C: common_vendor.o(clear),
        D: common_vendor.n(formats.value.direction === "rtl" ? "ql-active" : ""),
        E: common_vendor.o(format),
        F: common_vendor.o(onStatusChange),
        G: __props.readOnly,
        H: common_vendor.o(onEditorReady)
      };
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-0385b401"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/components/rich-text-edit/rich-text-edit.js.map
