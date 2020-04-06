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
package com.josedlpozo.galileo.realm.realmbrowser.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.josedlpozo.galileo.realm.R;
import com.josedlpozo.galileo.realm.realmbrowser.browser.BrowserContract;
import com.josedlpozo.galileo.realm.realmbrowser.browser.view.RealmBrowserActivity;
import com.josedlpozo.galileo.realm.realmbrowser.helper.DataHolder;
import com.josedlpozo.galileo.realm.realmbrowser.helper.Utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.RealmConfiguration;
import io.realm.RealmModel;
import io.realm.RealmObjectSchema;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

import static com.josedlpozo.galileo.realm.realmbrowser.helper.DataHolder.DATA_HOLDER_KEY_CONFIG;
import static com.josedlpozo.galileo.realm.realmbrowser.helper.DataHolder.DATA_HOLDER_KEY_FIELD;
import static com.josedlpozo.galileo.realm.realmbrowser.helper.DataHolder.DATA_HOLDER_KEY_OBJECT;

public class RealmObjectActivity extends AppCompatActivity {
    private static final String EXTRAS_REALM_MODEL_CLASS = "REALM_MODEL_CLASS";
    private static final String EXTRAS_FLAG_NEW_OBJECT = "NEW_OBJECT";

    private Class<? extends RealmModel> mRealmObjectClass;
    private DynamicRealmObject currentDynamicRealmObject;
    private List<Field> classFields;
    private RealmBrowserViewField primaryKeyFieldView;
    private HashMap<String, RealmBrowserViewField> fieldViewsList;
    @Nullable
    private DynamicRealm dynamicRealm;
    private LinearLayout linearLayout;

    public static Intent getIntent(Context context, Class<? extends RealmModel> realmModelClass, boolean newObject) {
        Intent intent = new Intent(context, RealmObjectActivity.class);
        intent.putExtra(EXTRAS_REALM_MODEL_CLASS, realmModelClass);
        intent.putExtra(EXTRAS_FLAG_NEW_OBJECT, newObject);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.realm_browser_ac_realm_object);
        RealmConfiguration configuration = (RealmConfiguration) DataHolder.getInstance().retrieve(


                DATA_HOLDER_KEY_CONFIG);
        if (configuration != null) {
            dynamicRealm = DynamicRealm.getInstance(configuration);
        }

        mRealmObjectClass = (Class<? extends RealmModel>) getIntent().getSerializableExtra(EXTRAS_REALM_MODEL_CLASS);

        // Get Extra
        if (!getIntent().getBooleanExtra(EXTRAS_FLAG_NEW_OBJECT, true)) {
            currentDynamicRealmObject = (DynamicRealmObject) DataHolder.getInstance().retrieve(DATA_HOLDER_KEY_OBJECT);
        }

        // Fill fields list
        RealmObjectSchema schema = dynamicRealm.getSchema().get(mRealmObjectClass.getSimpleName());
        classFields = new ArrayList<>();
        for (String s : schema.getFieldNames()) {
            try {
                classFields.add(mRealmObjectClass.getDeclaredField(s));
            } catch (NoSuchFieldException e) {
            }
        }

        // Init Views
        linearLayout = (LinearLayout) findViewById(R.id.realm_browser_linearLayout);
        fieldViewsList = new HashMap<>();
        int margin = this.getResources().getDimensionPixelSize(R.dimen.realm_browser_activity_margin);

        for (final Field field : classFields) {
            RealmBrowserViewField realmFieldView;
            if (Utils.isString(field)) {
                realmFieldView = new RealmBrowserViewString(this, schema, field);
            } else if (Utils.isNumber(field)) {
                realmFieldView = new RealmBrowserViewNumber(this, schema, field);
            } else if (Utils.isBoolean(field)) {
                realmFieldView = new RealmBrowserViewBool(this, schema, field);
            } else if (Utils.isBlob(field)) {
                realmFieldView = new RealmBrowserViewBlob(this, schema, field);
            } else if (Utils.isDate(field)) {
                realmFieldView = new RealmBrowserViewDate(this, schema, field);
            } else if (Utils.isParametrizedField(field)) {
                realmFieldView = new RealmBrowserViewRealmList(this, schema, field);
                realmFieldView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (currentDynamicRealmObject != null) {
                            DataHolder.getInstance().save(DATA_HOLDER_KEY_OBJECT, currentDynamicRealmObject);
                            DataHolder.getInstance().save(DATA_HOLDER_KEY_FIELD, field);
                            RealmBrowserActivity.Companion.start(RealmObjectActivity.this, BrowserContract.DisplayMode.Companion.REALM_LIST);
                        } else {
                            // TODO choose objects to add
                            Toast.makeText(RealmObjectActivity.this, "TODO", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else if (Utils.isRealmObjectField(field)) {
                realmFieldView = new RealmBrowserViewRealmObject(this, schema, field);
                realmFieldView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (currentDynamicRealmObject != null) {
                            // TODO start this activity
                            Toast.makeText(RealmObjectActivity.this, "TODO", Toast.LENGTH_SHORT).show();
                        } else {
                            // TODO choose object
                            Toast.makeText(RealmObjectActivity.this, "TODO", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                // Skip this field.
                continue;
            }
            realmFieldView.setPadding(margin, margin / 2, margin, margin / 2);

            if (dynamicRealm.getSchema().get(mRealmObjectClass.getSimpleName()).isPrimaryKey(field.getName())) {
                primaryKeyFieldView = realmFieldView;
            }

            // Add the object to the view for setting the current value etc.
            if (currentDynamicRealmObject != null) {
                realmFieldView.setRealmObject(currentDynamicRealmObject);
            }

            // Add View
            linearLayout.addView(realmFieldView);
            fieldViewsList.put(field.getName(), realmFieldView);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            if (currentDynamicRealmObject == null) {
                actionBar.setTitle(String.format("New %s", mRealmObjectClass.getSimpleName()));
            } else {
                actionBar.setTitle(String.format("%s", mRealmObjectClass.getSimpleName()));
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.realm_browser_menu_objectactivity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (currentDynamicRealmObject == null) {
            menu.findItem(R.id.realm_browser_action_delete).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.realm_browser_action_save) {
            if (saveObject()) {
                Snackbar.make(linearLayout, "Saved Changes.", Snackbar.LENGTH_SHORT).show();
            }
            return true;
        } else if (item.getItemId() == R.id.realm_browser_action_delete) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete");
            builder.setMessage(String.format("Are you sure you want to delete this %s object?", mRealmObjectClass.getSimpleName()));
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dynamicRealm.beginTransaction();
                    currentDynamicRealmObject.deleteFromRealm();
                    dynamicRealm.commitTransaction();
                    dialogInterface.dismiss();
                    finish();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dynamicRealm != null && !dynamicRealm.isClosed()) {
            dynamicRealm.close();
        }
    }

    @Nullable
    private DynamicRealmObject createObject() {
        if (dynamicRealm == null) return null;

        DynamicRealmObject newRealmObject = null;

        // Start Realm Transaction
        dynamicRealm.beginTransaction();

        // Create object
        if (dynamicRealm.getSchema().get(mRealmObjectClass.getSimpleName()).hasPrimaryKey()) {
            try {
                String primaryKeyFieldName = Utils.getPrimaryKeyFieldName(dynamicRealm.getSchema().get(mRealmObjectClass.getSimpleName()));
                newRealmObject = dynamicRealm.createObject(mRealmObjectClass.getSimpleName(), fieldViewsList.get(primaryKeyFieldName).getValue());
            } catch (IllegalArgumentException e) {
                dynamicRealm.cancelTransaction();
                Snackbar.make(linearLayout, "Error creating Object: IllegalArgumentException", Snackbar.LENGTH_SHORT).show();
            } catch (RealmPrimaryKeyConstraintException e) {
                fieldViewsList.get(Utils.getPrimaryKeyFieldName(dynamicRealm.getSchema().get(mRealmObjectClass.getSimpleName()))).togglePrimaryKeyError(true);
                dynamicRealm.cancelTransaction();
                Snackbar.make(linearLayout, "Error creating Object: PrimaryKeyConstraintException", Snackbar.LENGTH_SHORT).show();
            }
        } else {
            newRealmObject = dynamicRealm.createObject(mRealmObjectClass.getSimpleName());
        }

        // Commit Realm Transaction
        if (dynamicRealm.isInTransaction()) {
            dynamicRealm.commitTransaction();
        }

        return newRealmObject;
    }

    private boolean saveObject() {
        if (dynamicRealm == null) {
            return false;
        }

        // Return if any field holds a invalid value
        for (String fieldName : fieldViewsList.keySet()) {
            if (!fieldViewsList.get(fieldName).isInputValid()) {
                return false;
            }
        }

        RealmObjectSchema realmObjectSchema = dynamicRealm.getSchema().get(mRealmObjectClass.getSimpleName());

        DynamicRealmObject newRealmObject = null;
        if (currentDynamicRealmObject == null || (realmObjectSchema.hasPrimaryKey() && (!Utils
                .equals(currentDynamicRealmObject.get(realmObjectSchema.getPrimaryKey()), primaryKeyFieldView.getValue())))) {
            // PK has been changed or don't have and old object to change -> create new object
            newRealmObject = createObject();
            if (newRealmObject == null) {
                return false;
            }
        }

        dynamicRealm.beginTransaction();
        // Set values
        for (String fieldName : fieldViewsList.keySet()) {
            if (realmObjectSchema.isPrimaryKey(fieldName)) {
                continue; // Prevent changing of PK, should be handled with creation of new object with new PK
            }
            if (!realmObjectSchema.isNullable(fieldName) && fieldViewsList.get(fieldName).getValue() == null) {
                throw new IllegalStateException("A view which holds a nonnullable field must not return null.");
            }
            if (newRealmObject == null) {
                currentDynamicRealmObject.set(fieldName, fieldViewsList.get(fieldName).getValue());
            } else {
                newRealmObject.set(fieldName, fieldViewsList.get(fieldName).getValue());
            }
        }

        // Delete old object if new object was created
        if (newRealmObject != null && currentDynamicRealmObject != null && currentDynamicRealmObject.isManaged()) {
            currentDynamicRealmObject.deleteFromRealm();
            currentDynamicRealmObject = newRealmObject;
        }

        // Update views
        for (RealmBrowserViewField viewField : this.fieldViewsList.values()) {
            viewField.setRealmObject(currentDynamicRealmObject);
        }

        dynamicRealm.commitTransaction();

        // Reset primary key error
        RealmBrowserViewField primaryKeyView = fieldViewsList.get(
                Utils.getPrimaryKeyFieldName(dynamicRealm.getSchema().get(mRealmObjectClass.getSimpleName())));
        if (primaryKeyView != null) {
            primaryKeyView.togglePrimaryKeyError(false);
        }

        return true;
    }
}
