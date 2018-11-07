/*
 * The MIT License (MIT)
 *
 * Original Work: Copyright (c) 2015 Danylyk Dmytro
 *
 * Modified Work: Copyright (c) 2015 Rottmann, Jonas
 *
 * Modified Work: Copyright (c) 2018 vicfran
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.josedlpozo.galileo.realm.realmbrowser.files;

import android.content.Context;
import android.support.annotation.NonNull;
import com.josedlpozo.galileo.realm.realmbrowser.basemvp.BaseInteractor;
import com.josedlpozo.galileo.realm.realmbrowser.basemvp.BasePresenter;
import com.josedlpozo.galileo.realm.realmbrowser.basemvp.BaseView;
import com.josedlpozo.galileo.realm.realmbrowser.files.model.FilesPojo;
import java.util.ArrayList;

public interface FilesContract {

    interface View extends BaseView<Presenter> {
        void updateWithFiles(@NonNull ArrayList<FilesPojo> filesList);

        Context getViewContext();

        void showToast(String message);
    }

    interface Presenter extends BasePresenter<View> {
        void requestForContentUpdate(@NonNull Context context);

        void onFileSelected(FilesPojo item);

        void updateWithFiles(ArrayList<FilesPojo> filesList);
    }


    interface Interactor extends BaseInteractor {
        void requestForContentUpdate(@NonNull Context context);
    }
}
