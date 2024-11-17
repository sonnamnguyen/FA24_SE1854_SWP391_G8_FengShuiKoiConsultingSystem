import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Line } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend } from 'chart.js';
import SidebarAdmin from "./layouts/slideBar";
import FooterAdmin from "./layouts/footerAdmin";
import { getToken } from "../service/localStorageService";

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
);

interface BillData {
  month: number;
  totalMoney: number;
}

const Dashboard = () => {
  const [bill, setBill] = useState<BillData[]>([]);
  const [loading, setLoading] = useState(true);

  // Fetching income data from the API
  const fetchIncomeData = async (): Promise<BillData[] | null> => {
    try {
      const token = getToken();
      const response = await axios.get('http://localhost:9090/api/bills/total-income-between-dates', {
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      });

      if (response.data.code === 1000) {
        return response.data.result.map((bill: [number, number]) => ({
          month: bill[0],
          totalMoney: bill[1],
        })) as BillData[];
      }
      console.error("Failed to fetch income data: ", response.data.message);
      return null;
    } catch (error) {
      console.error("Error fetching income data: ", error);
      return null;
    }
  };

  useEffect(() => {
    const loadData = async () => {
      const incomeData = await fetchIncomeData();
      if (incomeData) {
        setBill(incomeData);
      }
      setLoading(false);
    };

    loadData();
  }, []);

  const chartData = {
    labels: bill.map(item => `Month ${item.month}`),
    datasets: [
      {
        label: 'Total Income',
        data: bill.map(item => item.totalMoney),
        borderColor: 'rgba(75, 192, 192, 1)',
        backgroundColor: 'rgba(75, 192, 192, 0.2)',
        tension: 0.1,
      },
    ],
  };

  const options = {
    responsive: true,
    plugins: {
      title: {
        display: true,
        text: 'Total Income Per Month (2024)',
      },
    },
    scales: {
      x: {
        title: {
          display: true,
          text: 'Month',
        },
      },
      y: {
        title: {
          display: true,
          text: 'Total Income (VND)',
        },
      },
    },
  };

  return (
    <div>
      <SidebarAdmin />
      <h1>Dashboard</h1>
      {loading ? (
        <p>Loading data...</p>
      ) : (
        <div style={{ width: '1200px', height: '800px', marginLeft: '250px' }}>
          <Line data={chartData} options={options} />
        </div>
      )}
      <FooterAdmin />
    </div>
  );
};

export default Dashboard;
