/**
 * @externs
 */

/**
 * @param {(number|DOMMatrixReadOnly)} m11OrMatrix
 * @param {number=} m12
 * @param {number=} m21
 * @param {number=} m22
 * @param {number=} dx
 * @param {number=} dy
 * @return {undefined}
 */
CanvasRenderingContext2D.prototype.setTransform = function(
    m11OrMatrix, m12, m21, m22, dx, dy) {};

// we adds the method to `CanvasRenderingContext2D` because we can't replace methods in `BaseRenderingContext2D`