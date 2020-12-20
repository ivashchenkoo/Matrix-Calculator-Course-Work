package com.example.matrixcalculatorcw;

import android.os.Parcel;
import android.os.Parcelable;

import Jama.Matrix;

public class ItemMatrix implements Parcelable {
    public static final Parcelable.Creator<ItemMatrix> CREATOR = new Parcelable.Creator<ItemMatrix>() {
        @Override
        public ItemMatrix createFromParcel(Parcel in) {
            return new ItemMatrix(in);
        }

        @Override
        public ItemMatrix[] newArray(int size) {
            return new ItemMatrix[size];
        }
    };
    protected String name;
    protected Matrix matrix;

    ItemMatrix(ItemMatrix itemMatrix) {
        name = itemMatrix.name;
        matrix = itemMatrix.matrix;
    }

    ItemMatrix(String name, Matrix matrix) {
        this.name = name;
        this.matrix = matrix;
    }

    private ItemMatrix(Parcel in) {
        name = in.readString();
        matrix = (Matrix) in.readValue(Matrix.class.getClassLoader());
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Matrix getMatrix() {
        return this.matrix;
    }

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }

    int getRow() {
        return matrix.getRowDimension();
    }

    int getColumn() {
        return matrix.getColumnDimension();
    }

    double getCell(int i, int j) {
        return matrix.get(i, j);
    }

    int[] getDimensionArray() {
        return new int[]{getRow(), getColumn()};
    }

    double determinant() {
        try {
            return matrix.det();
        } catch (Exception e) {
            return Double.NaN;
        }
    }

    int rank() {
        return this.matrix.rank();
    }

    double trace() {
        return this.matrix.trace();
    }

    Matrix inverse() {
        return getMatrix().inverse();
    }

    Matrix times(ItemMatrix itemMatrix) {
        return getMatrix().times(itemMatrix.getMatrix());
    }

    Matrix plus(ItemMatrix itemMatrix) {
        return getMatrix().plus(itemMatrix.getMatrix());
    }

    Matrix minus(ItemMatrix itemMatrix) {
        return getMatrix().minus(itemMatrix.getMatrix());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeValue(matrix);
    }
}