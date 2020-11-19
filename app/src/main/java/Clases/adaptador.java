package Clases;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.underpro.descarga.R;

import java.util.List;

public class adaptador extends RecyclerView.Adapter<adaptador.ViewHolder> implements  View.OnClickListener{
    public List<presetdata> listado;
    private View.OnClickListener listener;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView Version;
        public TextView fecha;
        public ImageView Icono;
        public CardView cv;

        public ViewHolder(View itemView) {
            super(itemView);
            this.Version = (TextView) itemView.findViewById(R.id.version);
            this.fecha = (TextView) itemView.findViewById(R.id.fecha);
            this.Icono = (ImageView) itemView.findViewById(R.id.foto);
         /*   cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println(getAdapterPosition());
                }
            });*/
        }
    }
    public adaptador(List<presetdata> listado2) {
        this.listado = listado2;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler, parent, false);
        view.setOnClickListener(this);
        ViewHolder viewHolder=new ViewHolder(view);
        return  viewHolder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.Version.setText(this.listado.get(position).getVersion());
        holder.fecha.setText(this.listado.get(position).getFecha());
        holder.Icono.setImageResource(this.listado.get(position).getIcono());
    }
    public int getItemCount() {
        return this.listado.size();
    }
    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }
    public void onClick(View view) {
            if(listener!=null){
                listener.onClick(view);
            }
    }



}