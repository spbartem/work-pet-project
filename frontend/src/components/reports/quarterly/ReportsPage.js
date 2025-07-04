import React from "react";
import * as Tabs from "@radix-ui/react-tabs";
import Procuracy from "./Procuracy";
const backendUrl = process.env.REACT_APP_API_URL; // Задаем URL бэкенда

console.log(backendUrl)

const ReportsPage = () => {
  return (
    <div className="p-6 bg-gray-100 rounded-lg shadow-md">
      <Tabs.Root defaultValue="Procuracy" className="w-full">
        {/* Навигация по вкладкам */}
        <Tabs.List className="flex border-b border-gray-300">
          <Tabs.Trigger
            value="Procuracy"
            className="px-4 py-2 text-gray-600 hover:text-gray-800 border-b-2 border-transparent hover:border-blue-500 
            transition-all focus:outline-none data-[state=active]:border-blue-500 data-[state=active]:text-blue-600"
          >
            Прокуратура
          </Tabs.Trigger>
          <Tabs.Trigger
            value="report2"
            className="px-4 py-2 text-gray-600 hover:text-gray-800 border-b-2 border-transparent hover:border-blue-500 
            transition-all focus:outline-none data-[state=active]:border-blue-500 data-[state=active]:text-blue-600"
          >
            Отчёт 2
          </Tabs.Trigger>
          <Tabs.Trigger
            value="report3"
            className="px-4 py-2 text-gray-600 hover:text-gray-800 border-b-2 border-transparent hover:border-blue-500 
            transition-all focus:outline-none data-[state=active]:border-blue-500 data-[state=active]:text-blue-600"
          >
            Отчёт 3
          </Tabs.Trigger>
        </Tabs.List>

        {/* Контент вкладок */}
        <Tabs.Content value="Procuracy" className="p-4 bg-white rounded-b-lg shadow">
          <h2 className="text-lg font-semibold text-gray-800">Отчёт по собираемости</h2>
          <Procuracy apiEndpoint={`${backendUrl}/api/reports/quarterly/procuracy`} />
        </Tabs.Content>

        <Tabs.Content value="report2" className="p-4 bg-white rounded-b-lg shadow">
          <h2 className="text-lg font-semibold text-gray-800">Отчёт по платежам</h2>
          
        </Tabs.Content>

        <Tabs.Content value="report3" className="p-4 bg-white rounded-b-lg shadow">
          <h2 className="text-lg font-semibold text-gray-800">Отчёт по задолженностям</h2>
         
        </Tabs.Content>
      </Tabs.Root>
    </div>
  );
};

export default ReportsPage;
