package nickvenis.sherbert;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class bench extends AppCompatActivity {
    sqlhelperbench benchdb;
    Button btnViewAll, btnAddRecord;
    EditText editWeight, editReps, editDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bench);

        ImageView i = (ImageView) findViewById(R.id.opensettings);
        i.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent newactivity = new Intent(bench.this, home2.class);
                startActivity(newactivity);
            }
        });
        benchdb = new sqlhelperbench(this);
        btnViewAll = (Button) findViewById(R.id.viewallbench);
        btnAddRecord = (Button) findViewById(R.id.addrecord);
        editWeight = (EditText)  findViewById(R.id.editTextWeight);
        editReps = (EditText)  findViewById(R.id.editTextReps);
        editDate = (EditText)  findViewById(R.id.editTextDate);
        viewall();
        addRecord();



    }
    public void viewall(){
        btnViewAll.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor res = benchdb.getallinfo();
                        if(res.getCount()==0){
                            showbench("Error", "Add some data first!");
                            return;
                        }
                        StringBuffer buffer = new StringBuffer();
                        while (res.moveToNext()){
                            buffer.append("Date: "+res.getString(1)+"\n");
                            buffer.append("Weight: "+res.getString(2)+"kg"+"\n");
                            buffer.append("Reps: "+res.getString(3)+"\n\n");
                        }
                        showbench("Found user data:",buffer.toString());
                    }
                }
        );

    }
    public void showbench(String title, String message){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setCancelable(true);
        b.setTitle(title);
        b.setMessage(message);
        b.show();

    }
    public void addRecord(){
        btnAddRecord.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    boolean isInserted = benchdb.addrecord(editDate.getText().toString(),
                            Integer.parseInt(editWeight.getText().toString()),
                            Integer.parseInt(editReps.getText().toString()));
                        if(isInserted= true){
                            Toast.makeText(bench.this,"Data Added!",Toast.LENGTH_LONG).show();
                            editDate.setText("");
                            editWeight.setText("");
                            editReps.setText("");
                        } else
                            Toast.makeText(bench.this,"Data cannot be added",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

}
