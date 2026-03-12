window.parseUrls = function parseUrls(urlString) {
  if (!urlString) return [];
  //如果不存在,则在最后面追加1个,
  urlString = urlString + "";

  if (!urlString.endsWith(",")) {
    urlString += ",";
  }
  return urlString
    .split(",")
    .filter((url) => url.trim() !== "")
    .map((url) => {
      const pathParts = url.split("/");
      const fileName = pathParts[pathParts.length - 1];
      const fileExtension = fileName.split(".").pop().toLowerCase();

      return {
        name: fileName,
        type: fileExtension,
        url: url.trim(),
      };
    });
};

/**
 * 得到完整的时间格式 年-月-日 时分秒
 */
window.YMDHMSFormat = function YMDHMSFormat(val) {
  if (!val) return "";
  let date = new Date(val);
  if (!date) return "";

  var y = date.getFullYear();
  var m = (date.getMonth() + 1).toString();
  m = parseInt(m) < 10 ? "0" + m : m;
  var d = date.getDate().toString();
  d = parseInt(d) < 10 ? "0" + d : d;
  var h = date.getHours().toString();
  h = parseInt(h) < 10 ? "0" + h : h;
  var minute = date.getMinutes().toString();
  minute = parseInt(minute) < 10 ? "0" + minute : minute;
  var second = date.getSeconds().toString();
  second = parseInt(second) < 10 ? "0" + second : second;
  return y + "-" + m + "-" + d + " " + h + ":" + minute + ":" + second;
};
/**
 * 得到完整的时间格式 年-月-日
 */
window.YMDFormat = function YMDFormat(val) {
  if (!val) return "";
  let date = new Date(val);
  if (!date) return "";

  var y = date.getFullYear();
  var m = (date.getMonth() + 1).toString();
  m = parseInt(m) < 10 ? "0" + m : m;
  var d = date.getDate().toString();
  d = parseInt(d) < 10 ? "0" + d : d;
  var h = date.getHours().toString();
  h = parseInt(h) < 10 ? "0" + h : h;
  var minute = date.getMinutes().toString();
  minute = parseInt(minute) < 10 ? "0" + minute : minute;
  var second = date.getSeconds().toString();
  second = parseInt(second) < 10 ? "0" + second : second;
  return y + "-" + m + "-" + d + " ";
};

/**
 * 得到时分秒格式
 */
window.HMSFormat = function HMSFormat(val) {
  if (!val) return "";
  let date = new Date(val);
  if (!date) return "";

  var y = date.getFullYear();
  var m = (date.getMonth() + 1).toString();
  m = parseInt(m) < 10 ? "0" + m : m;
  var d = date.getDate().toString();
  d = parseInt(d) < 10 ? "0" + d : d;
  var h = date.getHours().toString();
  h = parseInt(h) < 10 ? "0" + h : h;
  var minute = date.getMinutes().toString();
  minute = parseInt(minute) < 10 ? "0" + minute : minute;
  var second = date.getSeconds().toString();
  second = parseInt(second) < 10 ? "0" + second : second;
  return h + ":" + minute + ":" + second;
};
/**
 * 获取路径中的文件名称
 */
window.GetFileNameByPath = function GetFileNameByPath(path) {
  if (!path) {
    return "";
  }
  var pos1 = path.lastIndexOf("/");
  var pos2 = path.lastIndexOf("\\");
  var pos = Math.max(pos1, pos2);
  if (pos < 0) {
    return path;
  } else {
    return path.substring(pos + 1);
  }
};
/**
 * 获取路径中的文件格式
 */
window.GetFileTypeByPath = function GetFileTypeByPath(path) {
  if (!path) {
    return "";
  }
  var pos = path.lastIndexOf(".");
  if (pos < 0) {
    return "";
  } else {
    return path.substring(pos + 1);
  }
};
/**
 * 将url数组转换为对象数组
 */
window.FullConvertUrlArray = function FullConvertUrlArray(url) {
  if (!url) return [];
  return url
    .split(",")
    .filter((url) => url.trim() !== "")
    .map((url) => {
      const pathParts = url.split("/");
      const fileName = pathParts[pathParts.length - 1];
      const fileExtension = fileName.split(".").pop().toLowerCase();

      return {
        name: fileName,
        type: fileExtension,
        url: url.trim(),
      };
    });
};
/**
 * 保留2位小数
 */
window.ToFixed2 = function ToFixed2(value) {
  if (value) {
    return value.toFixed(2);
  }
  return "0.00";
};

/**
 * 保留3位小数
 */
window.ToFixed3 = function ToFixed3(value) {
  if (value) {
    return value.toFixed(3);
  }
  return "0.000";
};
/**
 * 手机号格式化
 */
window.PhoneFormat = function PhoneFormat(value) {
  if (value) {
    return value.replace(/(\d{3})\d{4}(\d{4})/, "$1****$2");
  }
  return "";
};
/**
 * 姓名格式化
 */
window.NameFormat = function NameFormat(value) {
  if (value) {
    if (value.length <= 1) {
      return value;
    }
    const firstName = value.charAt(0);
    const restName = value.substring(1);
    const stars = "*".repeat(restName.length);
    return firstName + stars;
  }
  return "";
};
/**
 * 替换html标签
 */
window.RepalceHtml = function RepalceHtml(value) {
  if (value) {
    return value.replace(/<[^>]+>|&[^>]+;/g, "").trim();
  }
};
