//
// Copyright (c) 2020 Couchbase, Inc All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
package com.couchbase.android.listsync.ui.p2p.nearby;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.couchbase.android.listsync.databinding.FragmentNearbyBinding;
import com.couchbase.android.listsync.ui.p2p.BaseFragment;


public final class NearbyFragment extends BaseFragment {
    @SuppressWarnings("NotNullFieldNotInitialized")
    @NonNull
    private NearbyViewModel viewModel;

    @SuppressWarnings("NotNullFieldNotInitialized")
    @NonNull
    private FragmentNearbyBinding binding;

    @SuppressWarnings("NotNullFieldNotInitialized")
    @NonNull
    private NearbyAdapter nearbyAdapter;

    @NonNull
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle state) {
        viewModel = getViewModel(NearbyViewModel.class);

        binding = FragmentNearbyBinding.inflate(inflater, container, false);
        final View root = binding.getRoot();

        nearbyAdapter = NearbyAdapter.setup(getActivity(), binding.nearby, viewModel::selectNearby);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel.getNearby().observe(this, nearbyAdapter::populate);
    }

    @Override
    public void onStop() {
        super.onStop();
        viewModel.cancel();
    }
}