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
package com.couchbase.android.listsync.ui.p2p.server;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import java.net.URI;
import java.util.Set;

import com.couchbase.android.listsync.databinding.FragmentServerBinding;
import com.couchbase.android.listsync.ui.p2p.BaseFragment;
import com.couchbase.android.listsync.ui.p2p.SyncAdapter;


public final class ServerFragment extends BaseFragment {
    @SuppressWarnings("NotNullFieldNotInitialized")
    @NonNull
    private ServerViewModel viewModel;

    @SuppressWarnings("NotNullFieldNotInitialized")
    @NonNull
    private FragmentServerBinding binding;

    @SuppressWarnings("NotNullFieldNotInitialized")
    @NonNull
    private SyncAdapter adapter;

    @NonNull
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle state) {
        viewModel = getViewModel(ServerViewModel.class);

        binding = FragmentServerBinding.inflate(inflater, container, false);
        final View root = binding.getRoot();

        binding.start.setOnClickListener(v -> startServer());
        binding.stop.setOnClickListener(v -> stopServer());

        adapter = SyncAdapter.setup(getActivity(), binding.servers);

        return root;
    }

    @Override
    public void onStop() {
        super.onStop();
        viewModel.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.getServers().observe(this, adapter::populate);
    }

    private void startServer() {
        binding.start.setEnabled(false);
        binding.stop.setEnabled(true);

        final LiveData<Set<URI>> endpointData = viewModel.startServer();
        endpointData.observe(getViewLifecycleOwner(), new OneShotObserver<>(endpointData, adapter::populate));
    }

    private void stopServer() {
        final URI uri = adapter.getFirstConnection();
        if (uri == null) { return; }

        final LiveData<Set<URI>> endpointData = viewModel.stopServer(uri);
        endpointData.observe(getViewLifecycleOwner(), new OneShotObserver<>(endpointData, this::onServerStopped));
    }

    private void onServerStopped(@NonNull Set<URI> uris) {
        adapter.populate(uris);

        binding.start.setEnabled(true);
        binding.stop.setEnabled(false);
    }
}