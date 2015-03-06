package com.felkertech.n.dex.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.felkertech.n.dex.R;
import com.felkertech.n.dex.data.Evolution;
import com.felkertech.n.dex.data.Move;
import com.felkertech.n.dex.data.ParsedCsv;
import com.felkertech.n.dex.data.Pokemon;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.AnimateGifMode;

import java.net.MalformedURLException;

/**
 * Created by N on 12/29/2014.
 */
public class PokemonDialog extends AlertDialog {
    Pokemon p;
    Context mContext;
    ParsedCsv pokemon_species;
    private static final String TAG = "PokemonDialog";

    public PokemonDialog(Context context, Pokemon pokemon, ParsedCsv pokemon_species) {
        super(context);
        p = pokemon;
        mContext = context;
        this.pokemon_species = pokemon_species;
    }

    @Override
    public void onCreate(Bundle saved) {
        LinearLayout cv = (LinearLayout) getLayoutInflater().inflate(R.layout.list_card_2, null, false);
        prepare(cv);
        setView(cv);
        setContentView(cv);
    }

    public void prepare(View convertView) {
//        Toast.makeText(mContext, "Prepare", Toast.LENGTH_SHORT).show();
//        Toast.makeText(mContext, p.species_name.substring(0, 1).toUpperCase() + p.species_name.substring(1), Toast.LENGTH_SHORT).show();
        setTitle(p.getFormName() + "   (" + p.species_id + ")");
        ((TextView) convertView.findViewById(R.id.species_id)).setText("#" + p.species_id);
        ((TextView) convertView.findViewById(R.id.identifier)).setText(p.getSpecies_name());
//        Toast.makeText(mContext, "H/W", Toast.LENGTH_SHORT).show();
        ((TextView) convertView.findViewById(R.id.height)).setText(p.getHeightFt());
        ((TextView) convertView.findViewById(R.id.weight)).setText(p.getWeightLbs());
        Log.d(TAG, p.self_types.toString()+" "+p.pokemon_id+" "+p.species_id+" "+p.species_name);
        if (p.self_types.size() == 1) {
            ((TextView) convertView.findViewById(R.id.type1)).setText(p.self_types.get(0).toUpperCase() + "");
            int c = getColorFromType(p.self_types.get(0));
            ((TextView) convertView.findViewById(R.id.type1)).setTextColor(c);
            ((TextView) convertView.findViewById(R.id.type2)).setText(" ");
        } else {
            ((TextView) convertView.findViewById(R.id.type1)).setText(p.self_types.get(0).toUpperCase() + "");
            ((TextView) convertView.findViewById(R.id.type2)).setText(" " + p.self_types.get(1).toUpperCase());
            int c = getColorFromType(p.self_types.get(0));
            ((TextView) convertView.findViewById(R.id.type1)).setTextColor(c);
            int c2 = getColorFromType(p.self_types.get(1));
            ((TextView) convertView.findViewById(R.id.type2)).setTextColor(c2);
        }
//        Toast.makeText(mContext, "Other textviews", Toast.LENGTH_SHORT).show();
        TextView abilityTextView = ((TextView) convertView.findViewById(R.id.abilities));
        abilityTextView.setText("");
        for(String ability: p.ability) {
            abilityTextView.setText(abilityTextView.getText()+"\n"+ability.substring(0,1).toUpperCase()+ability.substring(1));
        }
        TextView eggGroupTextView = ((TextView) convertView.findViewById(R.id.egg_groups));
        eggGroupTextView.setText("");
        for(String egg_group: p.egggroup) {
            eggGroupTextView.setText(eggGroupTextView.getText()+"\n"+egg_group.substring(0,1).toUpperCase()+egg_group.substring(1));
        }
        String ev = "";
        for (Evolution e : p.evolutions) {
            Log.d(TAG, e.toString());
            String pre = "";
            if(e.getPrevo_id() > -1)
                pre = pokemon_species.find("id", e.getPrevo_id() + "").getProperty("identifier");
            pre = pre.substring(0,1).toUpperCase()+pre.substring(1);

            String evo = pokemon_species.find("id", e.getEvo_id() + "").getProperty("identifier");
            evo = evo.substring(0,1).toUpperCase()+evo.substring(1);
            if (e.getMethod().isEmpty())
                ev = ev + evo;
            else
                ev = ev + pre + " (" + e.getMethod() + ") " + evo;
            ev = ev+"\n";
            Log.d(TAG, pre+" -> "+e.getMethod()+" -> "+evo);
            Log.d(TAG, ev);
        }
        if (p.evolutions.size() == 1)
            convertView.findViewById(R.id.evolutions).setVisibility(View.GONE);
        ((TextView) convertView.findViewById(R.id.evolutions)).setText(ev); //FIXME interpret

        ((TextView) convertView.findViewById(R.id.hp)).setText(p.hp + "");
        ((TextView) convertView.findViewById(R.id.atk)).setText(p.atk + "");
        ((TextView) convertView.findViewById(R.id.def)).setText(p.def + "");
        ((TextView) convertView.findViewById(R.id.spa)).setText(p.spa + "");
        ((TextView) convertView.findViewById(R.id.spd)).setText(p.spd + "");
        ((TextView) convertView.findViewById(R.id.spe)).setText(p.spe + "");

        //Pokedex Entry is for mobile only
        ((TextView) convertView.findViewById(R.id.pokedex_entry)).setText(p.pokedex_entry);
        ((TextView) convertView.findViewById(R.id.genus)).setText(p.genus+" Pkmn");

        TextView move_list = (TextView) convertView.findViewById(R.id.move_list);
        move_list.setText("");
        /*for(Move m: p.self_moves) {
            move_list.setText(move_list.getText()+"\n"+m.getName()+" - "+m.getMethod());
        }*/

        //Sprite
        //FIXME Issue with gifs in Ion
        /*try {
            ((WebView) convertView.findViewById(R.id.sprite_web)).loadUrl(p.getModelURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }*/
        try {
            Log.d(TAG, p.getModelURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            Ion.with(((ImageView) convertView.findViewById(R.id.sprite)))
                        .animateGif(AnimateGifMode.ANIMATE)
                    .placeholder(mContext.getResources().getDrawable(R.drawable.ic_launcher))
                    .load(p.getModelURL());
            doNothing();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    public void doNothing() throws MalformedURLException {}

    public int getColorFromType(String type) {
        int c;
        if (type.equals("grass"))
            c = R.color.grass;
        else if (type.equals("poison"))
            c = R.color.poison;
        else if (type.equals("fire"))
            c = R.color.fire;
        else if (type.equals("water"))
            c = R.color.water;
        else if (type.equals("bug"))
            c = R.color.bug;
        else if (type.equals("flying"))
            c = R.color.flying;
        else if (type.equals("electric"))
            c = R.color.electric;
        else if (type.equals("ground"))
            c = R.color.ground;
        else if (type.equals("fighting"))
            c = R.color.fighting;
        else if (type.equals("rock"))
            c = R.color.rock;
        else if (type.equals("steel"))
            c = R.color.steel;
        else if (type.equals("dark"))
            c = R.color.dark;
        else if (type.equals("fairy"))
            c = R.color.fairy;
        else if (type.equals("dragon"))
            c = R.color.dragon;
        else if (type.equals("psychic"))
            c = R.color.psychic;
        else if (type.equals("ghost"))
            c = R.color.ghost;
        else if (type.equals("ice"))
            c = R.color.ice;
        else
            c = R.color.normal;
        return mContext.getResources().getColor(c);
    }
}