package com.fypapplication.fypapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.fypapplication.fypapp.R;
import com.fypapplication.fypapp.databinding.LayoutBookingsBinding;
import com.fypapplication.fypapp.models.Booking;

import java.util.ArrayList;

public class BookingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ArrayList<Booking> bookingArrayList;
    private final Context context;
    private final BookingInterface bookingInterface;

    public BookingAdapter(ArrayList<Booking> bookingArrayList, Context context, BookingInterface bookingInterface) {
        this.bookingArrayList = bookingArrayList;
        this.context = context;
        this.bookingInterface = bookingInterface;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        LayoutBookingsBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.layout_bookings, parent, false);

        return new BookingViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Booking booking = bookingArrayList.get(position);
        ((BookingViewHolder) holder).setBinding(booking);

        ((BookingViewHolder) holder).binding.item.setOnClickListener(v -> {

            bookingInterface.onBookingClicked(bookingArrayList.get(position));

        });
    }

    @Override
    public int getItemCount() {
        return bookingArrayList.size();
    }

    public interface BookingInterface {
        void onBookingClicked(Booking booking);
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {

        LayoutBookingsBinding binding;

        public BookingViewHolder(@NonNull LayoutBookingsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setBinding(Booking booking) {

            binding.setBooking(booking);
            binding.executePendingBindings();

        }
    }
}
