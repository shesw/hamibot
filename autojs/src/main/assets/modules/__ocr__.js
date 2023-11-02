module.exports = function (runtime, global) {
    const rtOcr = runtime.ocr;
    const ocr = {}

    ocr.init = function () {
        return rtOcr.init();
    }

    ocr.ocrImage = function (image) {
       return rtOcr.ocrImage(image);
    }

    ocr.ocrImage2Native = function (image) {
        return rtOcr.ocrImage2Native(runtime, global, image)
    }

    ocr.end = function () {
        return rtOcr.end();
    }
    return ocr;
}
