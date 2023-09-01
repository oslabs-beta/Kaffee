import { jsx as _jsx, jsxs as _jsxs, Fragment as _Fragment } from "react/jsx-runtime";
import React from 'react';
// import { useAppDispatch, useAppSelector } from '../redux/hooks.ts';
import { useDispatch, useSelector } from 'react-redux';
import { addCluster, removeCluster, setActiveCluster, } from '../reducers/clusterSlice.js';
// import type { ClusterObject } from '../reducers/clusterSlice.js';
export default function Clusters() {
    // const clusterList: Array<ClusterObject> = useAppSelector(
    const clusterList = useSelector((state) => state.clusters.list);
    // const dispatch = useAppDispatch();
    const dispatch = useDispatch();
    const handleAddCluster = () => {
        dispatch(addCluster());
    };
    const handleRemoveCluster = () => {
        dispatch(removeCluster());
    };
    // const handleSetActive = (clusterId: number) => {
    const handleSetActive = (clusterId) => {
        dispatch(setActiveCluster(clusterId));
    };
    return (_jsxs("nav", { id: 'clusters', children: [_jsx("header", { children: "Clusters" }), _jsx("ul", { children: clusterList.length ? (clusterList.map((cluster) => {
                    // clusterList.map((cluster: ClusterObject) => {
                    return (_jsx("li", { children: _jsxs("button", { onClick: () => handleSetActive(cluster.id), children: ["Cluster ", cluster.name] }) }));
                })) : (_jsx(_Fragment, {})) }), _jsxs("footer", { children: [_jsx("button", { onClick: handleAddCluster, children: "Add" }), _jsx("button", { onClick: handleRemoveCluster, children: "Remove" })] })] }));
}
//# sourceMappingURL=Clusters.js.map