package com.example.lifelocator360.FragmentManagement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lifelocator360.DataBaseManagement.Contact;
import com.example.lifelocator360.R;
import java.util.List;
import java.util.Random;

import static com.example.lifelocator360.FragmentManagement.ContactsFragment.colors;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {
    private List<Contact> contacts;
    private int randomColor;


    public static class ContactsViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView contactName;
        public TextView contactSurname;
        public TextView contactInitials;


        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.contactInitials);
            contactName = itemView.findViewById(R.id.contactName);
            contactSurname = itemView.findViewById(R.id.contactSurname);
            contactInitials = itemView.findViewById(R.id.initials);


        }
    }

    public ContactsAdapter(List<Contact> contacts) {
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_card_layout, parent, false);
        ContactsViewHolder contactsViewHolder = new ContactsViewHolder(view);

        return contactsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, int position) {
        Contact currentContact = contacts.get(position);

        randomColor = colors[new Random().nextInt(colors.length)];
        holder.imageView.setBackgroundColor(randomColor);
        holder.contactName.setText(currentContact.getName());
        holder.contactSurname.setText(currentContact.getSurname());
        holder.contactInitials.setText(currentContact.getName().charAt(0) + currentContact.getSurname().charAt(0));

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }
}