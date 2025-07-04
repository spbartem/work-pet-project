import React, { useState, useEffect } from "react";
import axios from "axios";
import { useTable, usePagination } from "react-table";

const backendUrl = process.env.REACT_APP_API_URL; // Задаем URL бэкенда

const EntPretenseBillStatTable = () => {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [page, setPage] = useState(0);
  const [billDate, setBillDate] = useState("");

  const fetchData = async () => {
    setLoading(true);
    try {
      const response = await axios.get(`${backendUrl}/api/ent-pretense-bill-stat`, {
        params: {
          billDate: billDate || null,
          page: page,
        },
      });
      setData(response.data.content);
    } catch (error) {
      console.error("Error fetching data:", error);
    }
    setLoading(false);
  };

  useEffect(() => {
    fetchData();
  }, [page, billDate]);

    // Форматирование числа с разделением по разрядам
  const formatNumber = (num) => {
    return num.toLocaleString(); // Форматирование с учетом локали (по умолчанию)
  };

  const formatDate = (dateString) => {
    const options = { year: 'numeric', month: '2-digit', day: '2-digit' };
    return new Date(dateString).toLocaleDateString('ru-RU', options); // Форматирование для России
  };

  const [firstRowDebtAmount, setFirstRowDebtAmount] = React.useState(null);

  const columns = React.useMemo(
    () => [
      { Header: "Дата долгового документа", 
        accessor: "billDate", 
        Cell: ({ value }) => formatDate(value)
      },
      { Header: "Отчётная дата", accessor: "reportDate", Cell: ({ value }) => formatDate(value) },
      { Header: "Кол-во ЛС с долгом", accessor: "debtLsCount", Cell: ({ value }) => formatNumber(value) },
      { Header: "Сумма к оплате", accessor: "dueAmount" , Cell: ({ value }) => formatNumber(value) },
      { Header: "Сумма задолженности", 
        accessor: "debtAmount" , 
        Cell: ({ value, row }) => {
          // Устанавливаем debtAmount первой строки
          if (row.index == 0 && firstRowDebtAmount === null) {
            setFirstRowDebtAmount(value);
          }
          return formatNumber(value);
        },
      },
      { Header: "Процент задолженности от исходной", 
        accessor: row => {
          // Если первая сумма задолженности существует, делим текущую сумму на неё
          return firstRowDebtAmount ? ((row.debtAmount / firstRowDebtAmount) * 100).toFixed(2) : "-";
        },
        Cell: ({ value }) => `${value}%`,
      },
      { Header: "ЛС, по которым уменьшилась задолженность",
        columns: [
          { Header: "частично, кол-во", accessor: "debtPartiallyPayCount" , Cell: ({ value }) => formatNumber(value) },
          { Header: "частично, сумма", accessor: "debtPartiallyPayAmount" , Cell: ({ value }) => formatNumber(value) },
          { Header: "полностью, кол-во", accessor: "debtFullPayCount" , Cell: ({ value }) => formatNumber(value) },
          { Header: "полностью, сумма", accessor: "debtFullPayAmount" , Cell: ({ value }) => formatNumber(value) },
        ],
      },
      { Header: "Итого",
        columns: [
          { Header: "Кол-во ЛС",
            accessor: row => row.debtPartiallyPayCount + row.debtFullPayCount,
            Cell: ({ value }) => formatNumber(value),
           },
           { Header: "Сумма",
            accessor: row => row.debtPartiallyPayAmount + row.debtFullPayAmount,
            Cell: ({ value }) => formatNumber(value),
           }
        ],
       },
      { Header: "Общая задолженность*",
        columns: [
          { Header: "Кол-во ЛС", accessor: "debtTotalCount", Cell: ({ value }) => formatNumber(value) },
          { Header: "Сумма", accessor: "debtTotalAmount", Cell: ({ value }) => formatNumber(value) }
        ],
      }
    ],
    [firstRowDebtAmount]
  );

  const {
    getTableProps,
    getTableBodyProps,
    headerGroups,
    rows,
    prepareRow,
    canPreviousPage,
    canNextPage,
    pageOptions,
    nextPage,
    previousPage,
    state: { pageIndex },
  } = useTable(
    { columns, data, initialState: { pageIndex: 0 } },
    usePagination
  );

  return (
    <div className="App">
      <input
        type="date"
        value={billDate}
        onChange={(e) => setBillDate(e.target.value)}
      />
      <button onClick={fetchData}>Filter</button>
      <table {...getTableProps()} style={{ width: "100%", border: "1px solid black" }}>
        <thead>
          {headerGroups.map((headerGroup) => (
            <tr {...headerGroup.getHeaderGroupProps()}>
              {headerGroup.headers.map((column) => (
                <th {...column.getHeaderProps()}>
                  {column.render("Header")}
                </th>
              ))}
            </tr>
          ))}
        </thead>
        <tbody {...getTableBodyProps()}>
          {rows.map((row) => {
            prepareRow(row);
            return (
              <tr {...row.getRowProps()}>
                {row.cells.map((cell) => (
                  <td {...cell.getCellProps()}>{cell.render("Cell")}</td>
                ))}
              </tr>
            );
          })}
        </tbody>
      </table>
      <div>
        <button onClick={() => previousPage()} disabled={!canPreviousPage}>
          Previous
        </button>
        <span>
          Page {pageIndex + 1} of {pageOptions.length}
        </span>
        <button onClick={() => nextPage()} disabled={!canNextPage}>
          Next
        </button>
      </div>
    </div>
  );
};

export default EntPretenseBillStatTable;
