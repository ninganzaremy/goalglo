import {useState} from "react";

export const useDashboardHook = () => {
   const [activeTab, setActiveTab] = useState("summary");

   const handleTabChange = (tab) => setActiveTab(tab);

   return {activeTab, handleTabChange};
};