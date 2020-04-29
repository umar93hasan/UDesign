package com.numad19f.udesign;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.PixelCopy;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.TRANSPARENT;
import static android.graphics.Color.parseColor;

public class ARActivity extends AppCompatActivity implements View.OnClickListener{

    ArFragment arFragment;
    private ModelRenderable sofa1Renderable, sofa2Renderable, chairRenderable, chair2Renderable,
            barTableRenderable, bedRenderable, bed2Renderable, benchRenderable, bluecabinetRenderable, cabinetRenderable, cabinet3Renderable,
            chair3Renderable, lampRenderable, plant1Renderable, plant2Renderable, shelfRenderable, tableRenderable, barstoolRenderable, treeRenderable, tvRenderable;

    ImageView sofa1, sofa2, chair, chair2, barTable, bed, bed2, bench,
            bluecabinet, cabinet, cabinet3, chair3, lamp, plant1, plant2, shelf, table, barstool,
            tree, tv;

    View arrayView[];
    int selected = 1;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);
        arFragment = (ArFragment)getSupportFragmentManager().findFragmentById(R.id.sceneform_ux_fragment);
        sofa1 =  (ImageView) findViewById(R.id.sofa1);
        sofa2 = (ImageView) findViewById(R.id.sofa2);
        chair =  (ImageView) findViewById(R.id.chair);
        chair2 = (ImageView) findViewById(R.id.chair2);
        barTable = (ImageView) findViewById(R.id.bartable);
        bed = (ImageView) findViewById(R.id.bed);
        bed2 = (ImageView) findViewById(R.id.bed2);
        bench = (ImageView) findViewById(R.id.bench);
        bluecabinet = (ImageView) findViewById(R.id.bluecabinet);
        cabinet = (ImageView) findViewById(R.id.cabinet);
        cabinet3 = (ImageView) findViewById(R.id.cabinet3);
        chair3 = (ImageView) findViewById(R.id.chair3);
        lamp = (ImageView) findViewById(R.id.lamp);
        plant1 = (ImageView) findViewById(R.id.plant1);
        plant2 = (ImageView) findViewById(R.id.plant2);
        shelf = (ImageView) findViewById(R.id.shelf);
        table = (ImageView) findViewById(R.id.table);
        barstool = (ImageView) findViewById(R.id.barstool);
        tree = (ImageView) findViewById(R.id.tree);
        tv =(ImageView) findViewById(R.id.tv);

        setArrayView();
        setClickListener();
        setUpModel();

        arFragment.setOnTapArPlaneListener(new BaseArFragment.OnTapArPlaneListener() {
            @Override
            public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {
                Anchor anchor = hitResult.createAnchor();
                AnchorNode anchorNode = new AnchorNode(anchor);
                anchorNode.setParent(arFragment.getArSceneView().getScene());
                createModel(anchorNode,selected);
            }
        });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            savePhoto();
        });


        FloatingActionButton clear = findViewById(R.id.clear);
        clear.setOnClickListener(view -> {
            List<Node> children = new ArrayList<>(arFragment.getArSceneView().getScene().getChildren());
            for (Node node : children) {
                if (node instanceof AnchorNode) {
                    if (((AnchorNode) node).getAnchor() != null) {
                        ((AnchorNode) node).getAnchor().detach();
                    }
                }
            }

            Toast.makeText(this, "Objects Cleared From Screen", Toast.LENGTH_SHORT).show();

        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void savePhoto() {
        ArSceneView view = arFragment.getArSceneView();
        final Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.ARGB_8888);
        final HandlerThread handlerThread = new HandlerThread("PixelCopier");
        handlerThread.start();
        PixelCopy.request(view, bitmap, (copyResult) -> {
            String imageURL;
            if (copyResult == PixelCopy.SUCCESS) {
                try {
                    imageURL =  MediaStore.Images.Media.insertImage(getContentResolver(),bitmap,"title","description");
                } catch (Exception e) {
                    Toast.makeText(ARActivity.this, e.toString(),
                            Toast.LENGTH_LONG).show();
                    return;
                }
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                        "Photo saved", Snackbar.LENGTH_LONG);
                snackbar.setAction("View in Gallery", v -> {
                    Uri uri =  Uri.parse( imageURL );
                    File imageFile = new File(getRealPath(uri));
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    Uri contentUri = FileProvider.getUriForFile(this, "com.numad19f.udesign.fileprovider", imageFile);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setDataAndType(contentUri,"image/*");
                    this.startActivity(intent);
                });
                snackbar.show();
            } else {
                Toast toast = Toast.makeText(ARActivity.this,
                        "Failed saving photo: " + copyResult, Toast.LENGTH_LONG);
                toast.show();
            }
            handlerThread.quitSafely();
        }, new Handler(handlerThread.getLooper()));
    }

    private String getRealPath(Uri contentURI) {
        String path;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            path = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            path = cursor.getString(index);
            cursor.close();
        }
        return path;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setUpModel() {
        ModelRenderable.builder()
                .setSource(this, R.raw.couchpoofypillows)
                .build().thenAccept(renderable -> sofa1Renderable = renderable)
                .exceptionally(

                        throwable -> {
                            Toast.makeText(this, "Unable to load model", Toast.LENGTH_SHORT).show();
                            return null;
                        }
                );

        ModelRenderable.builder()
                .setSource(this, R.raw.sofa2)
                .build().thenAccept(renderable -> sofa2Renderable = renderable)
                .exceptionally(

                        throwable -> {
                            Toast.makeText(this, "Unable to load model", Toast.LENGTH_SHORT).show();
                            return null;
                        }
                );

        ModelRenderable.builder()
                .setSource(this, R.raw.singlechair)
                .build().thenAccept(renderable -> chairRenderable = renderable)
                .exceptionally(

                        throwable -> {
                            Toast.makeText(this, "Unable to load model", Toast.LENGTH_SHORT).show();
                            return null;
                        }
                );

        ModelRenderable.builder()
                .setSource(this, R.raw.armchair_wing)
                .build().thenAccept(renderable -> chair2Renderable = renderable)
                .exceptionally(

                        throwable -> {
                            Toast.makeText(this, "Unable to load model", Toast.LENGTH_SHORT).show();
                            return null;
                        }
                );

        ModelRenderable.builder()
                .setSource(this, R.raw.barcusisine)
                .build().thenAccept(renderable -> barTableRenderable = renderable)
                .exceptionally(

                        throwable -> {
                            Toast.makeText(this, "Unable to load model", Toast.LENGTH_SHORT).show();
                            return null;
                        }
                );

        ModelRenderable.builder()
                .setSource(this, R.raw.bed)
                .build().thenAccept(renderable -> bedRenderable = renderable)
                .exceptionally(

                        throwable -> {
                            Toast.makeText(this, "Unable to load model", Toast.LENGTH_SHORT).show();
                            return null;
                        }
                );


        ModelRenderable.builder()
                .setSource(this, R.raw.bed2)
                .build().thenAccept(renderable -> bed2Renderable = renderable)
                .exceptionally(

                        throwable -> {
                            Toast.makeText(this, "Unable to load model", Toast.LENGTH_SHORT).show();
                            return null;
                        }
                );

        ModelRenderable.builder()
                .setSource(this, R.raw.bench)
                .build().thenAccept(renderable -> benchRenderable = renderable)
                .exceptionally(

                        throwable -> {
                            Toast.makeText(this, "Unable to load model", Toast.LENGTH_SHORT).show();
                            return null;
                        }
                );

        ModelRenderable.builder()
                .setSource(this, R.raw.bluecabinet)
                .build().thenAccept(renderable -> bluecabinetRenderable = renderable)
                .exceptionally(

                        throwable -> {
                            Toast.makeText(this, "Unable to load model", Toast.LENGTH_SHORT).show();
                            return null;
                        }
                );

        ModelRenderable.builder()
                .setSource(this, R.raw.cabinet)
                .build().thenAccept(renderable -> cabinetRenderable = renderable)
                .exceptionally(

                        throwable -> {
                            Toast.makeText(this, "Unable to load model", Toast.LENGTH_SHORT).show();
                            return null;
                        }
                );

        ModelRenderable.builder()
                .setSource(this, R.raw.tvcenter)
                .build().thenAccept(renderable -> cabinet3Renderable = renderable)
                .exceptionally(

                        throwable -> {
                            Toast.makeText(this, "Unable to load model", Toast.LENGTH_SHORT).show();
                            return null;
                        }
                );

        ModelRenderable.builder()
                .setSource(this, R.raw.chaiseorange)
                .build().thenAccept(renderable -> chair3Renderable = renderable)
                .exceptionally(

                        throwable -> {
                            Toast.makeText(this, "Unable to load model", Toast.LENGTH_SHORT).show();
                            return null;
                        }
                );

        ModelRenderable.builder()
                .setSource(this, R.raw.lamp2)
                .build().thenAccept(renderable -> lampRenderable = renderable)
                .exceptionally(

                        throwable -> {
                            Toast.makeText(this, "Unable to load model", Toast.LENGTH_SHORT).show();
                            return null;
                        }
                );

        ModelRenderable.builder()
                .setSource(this, R.raw.plant1)
                .build().thenAccept(renderable -> plant1Renderable = renderable)
                .exceptionally(

                        throwable -> {
                            Toast.makeText(this, "Unable to load model", Toast.LENGTH_SHORT).show();
                            return null;
                        }
                );


        ModelRenderable.builder()
                .setSource(this, R.raw.plant2)
                .build().thenAccept(renderable -> plant2Renderable = renderable)
                .exceptionally(

                        throwable -> {
                            Toast.makeText(this, "Unable to load model", Toast.LENGTH_SHORT).show();
                            return null;
                        }
                );

        ModelRenderable.builder()
                .setSource(this, R.raw.shelf)
                .build().thenAccept(renderable -> shelfRenderable = renderable)
                .exceptionally(

                        throwable -> {
                            Toast.makeText(this, "Unable to load model", Toast.LENGTH_SHORT).show();
                            return null;
                        }
                );


        ModelRenderable.builder()
                .setSource(this, R.raw.tablenew)
                .build().thenAccept(renderable -> tableRenderable = renderable)
                .exceptionally(

                        throwable -> {
                            Toast.makeText(this, "Unable to load model", Toast.LENGTH_SHORT).show();
                            return null;
                        }
                );


        ModelRenderable.builder()
                .setSource(this, R.raw.tabo)
                .build().thenAccept(renderable -> barstoolRenderable = renderable)
                .exceptionally(

                        throwable -> {
                            Toast.makeText(this, "Unable to load model", Toast.LENGTH_SHORT).show();
                            return null;
                        }
                );

        ModelRenderable.builder()
                .setSource(this, R.raw.tree)
                .build().thenAccept(renderable -> treeRenderable = renderable)
                .exceptionally(

                        throwable -> {
                            Toast.makeText(this, "Unable to load model", Toast.LENGTH_SHORT).show();
                            return null;
                        }
                );

        ModelRenderable.builder()
                .setSource(this, R.raw.tv)
                .build().thenAccept(renderable -> tvRenderable = renderable)
                .exceptionally(

                        throwable -> {
                            Toast.makeText(this, "Unable to load model", Toast.LENGTH_SHORT).show();
                            return null;
                        }
                );

    }

    private void setClickListener() {
        for(int i = 0; i< arrayView.length;i++) {
            arrayView[i].setOnClickListener(this);
        }
    }

    private void createModel(AnchorNode anchorNode, int selected) {
        if(selected == 1) {
            TransformableNode sofa1 = new TransformableNode(arFragment.getTransformationSystem());
            sofa1.setParent(anchorNode);
            sofa1.setRenderable(sofa1Renderable);
            sofa1.select();
        }

        if(selected == 2) {
            TransformableNode sofa2 = new TransformableNode(arFragment.getTransformationSystem());
            sofa2.setParent(anchorNode);
            sofa2.setRenderable(sofa2Renderable);
            sofa2.select();
        }

        if(selected == 3) {
            TransformableNode chair = new TransformableNode(arFragment.getTransformationSystem());
            chair.setParent(anchorNode);
            chair.setRenderable(chairRenderable);
            chair.select();
        }

        if(selected == 4) {
            TransformableNode chair2 = new TransformableNode(arFragment.getTransformationSystem());
            chair2.setParent(anchorNode);
            chair2.setRenderable(chair2Renderable);
            chair2.select();
        }

        if(selected == 5) {
            TransformableNode barTable = new TransformableNode(arFragment.getTransformationSystem());
            barTable.setParent(anchorNode);
            barTable.setRenderable(barTableRenderable);
            barTable.select();
        }

        if(selected == 6) {
            TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
            node.setParent(anchorNode);
            node.setRenderable(bedRenderable);
            node.select();
        }

        if(selected == 7) {
            TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
            node.setParent(anchorNode);
            node.setRenderable(bed2Renderable);
            node.select();
        }

        if(selected == 8) {
            TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
            node.setParent(anchorNode);
            node.setRenderable(benchRenderable);
            node.select();
        }

        if(selected == 9) {
            TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
            node.setParent(anchorNode);
            node.setRenderable(bluecabinetRenderable);
            node.select();
        }

        if(selected == 10) {
            TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
            node.setParent(anchorNode);
            node.setRenderable(cabinetRenderable);
            node.select();
        }

        if(selected == 11) {
            TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
            node.setParent(anchorNode);
            node.setRenderable(cabinet3Renderable);
            node.select();
        }

        if(selected == 12) {
            TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
            node.setParent(anchorNode);
            node.setRenderable(chair3Renderable);
            node.select();
        }

        if(selected == 13) {
            TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
            node.setParent(anchorNode);
            node.setRenderable(lampRenderable);
            node.select();
        }

        if(selected == 14) {
            TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
            node.setParent(anchorNode);
            node.setRenderable(plant1Renderable);
            node.select();
        }
        if(selected == 15) {
            TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
            node.setParent(anchorNode);
            node.setRenderable(plant2Renderable);
            node.select();
        }
        if(selected == 16) {
            TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
            node.setParent(anchorNode);
            node.setRenderable(shelfRenderable);
            node.select();
        }
        if(selected == 17) {
            TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
            node.setParent(anchorNode);
            node.setRenderable(tableRenderable);
            node.select();
        }
        if(selected == 18) {
            TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
            node.setParent(anchorNode);
            node.setRenderable(barstoolRenderable);
            node.select();
        }
        if(selected == 19) {
            TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
            node.setParent(anchorNode);
            node.setRenderable(treeRenderable);
            node.select();
        }

        if(selected == 20) {
            TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
            node.setParent(anchorNode);
            node.setRenderable(tvRenderable);
            node.select();
        }

    }

    private void setArrayView() {
        arrayView = new View[]{
                sofa1, sofa2, chair, chair2, barTable, bed, bed2, bench, bluecabinet,
                cabinet, cabinet3, chair3, lamp, plant1, plant2, shelf, table, barstool, tree, tv
        };
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.sofa1) {
            selected = 1;
            setBackground(view.getId());
        }
        else  if(view.getId() == R.id.sofa2) {
            selected = 2;
            setBackground(view.getId());

        }
        else  if(view.getId() == R.id.chair) {
            selected = 3;
            setBackground(view.getId());

        }
        else  if(view.getId() == R.id.chair2) {
            selected = 4;
            setBackground(view.getId());

        }
        else  if(view.getId() == R.id.bartable) {
            selected = 5;
            setBackground(view.getId());

        }

        else  if(view.getId() == R.id.bed) {
            selected = 6;
            setBackground(view.getId());

        }
        else  if(view.getId() == R.id.bed2) {
            selected = 7;
            setBackground(view.getId());

        }
        else  if(view.getId() == R.id.bench) {
            selected = 8;
            setBackground(view.getId());

        }
        else  if(view.getId() == R.id.bluecabinet) {
            selected = 9;
            setBackground(view.getId());

        }
        else  if(view.getId() == R.id.cabinet) {
            selected = 10;
            setBackground(view.getId());

        }
        else  if(view.getId() == R.id.cabinet3) {
            selected = 11;
            setBackground(view.getId());

        }
        else  if(view.getId() == R.id.chair3) {
            selected = 12;
            setBackground(view.getId());

        }
        else  if(view.getId() == R.id.lamp) {
            selected = 13;
            setBackground(view.getId());

        }
        else  if(view.getId() == R.id.plant1) {
            selected = 14;
            setBackground(view.getId());

        }
        else  if(view.getId() == R.id.plant2) {
            selected = 15;
            setBackground(view.getId());

        }

        else  if(view.getId() == R.id.shelf) {
            selected = 16;
            setBackground(view.getId());

        }
        else  if(view.getId() == R.id.table) {
            selected = 17;
            setBackground(view.getId());

        }

        else  if(view.getId() == R.id.barstool) {
            selected = 18;
            setBackground(view.getId());

        }
        else  if(view.getId() == R.id.tree) {
            selected = 19;
            setBackground(view.getId());

        }
        else  if(view.getId() == R.id.tv) {
            selected = 20;
            setBackground(view.getId());

        }

    }

    private void setBackground(int id) {
        for(int i = 0; i< arrayView.length; i ++) {
            if(arrayView[i].getId() == id) {
                arrayView[i].setBackgroundColor(parseColor("#80333639"));
            }
            else {
                arrayView[i].setBackgroundColor(TRANSPARENT);
            }
        }
    }
}
