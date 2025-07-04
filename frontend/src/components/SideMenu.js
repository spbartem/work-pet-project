import React from 'react';
import {
    Drawer,
    List,
    ListItem,
    ListItemIcon,
    ListItemText,
    Collapse,
    Divider,
} from '@mui/material';
import {
    Home as HomeIcon,
    ExpandLess,
    ExpandMore,
    Report as ReportIcon,
    UploadFile
} from '@mui/icons-material';
import { useNavigate } from 'react-router-dom'; // Импортируем useNavigate для навигации

const SideMenu = () => {
    const navigate = useNavigate(); // Хук для навигации
    const [open, setOpen] = React.useState({
        reports: false, // Состояние для подпунктов
    });

    const handleToggle = (menu) => {
        setOpen((prevState) => ({ ...prevState, [menu]: !prevState[menu] }));
    };

    const handleMenuClick = (path) => {
        console.log(`Navigating to ${path}`);
        navigate(path); // Выполняем переход на указанный путь
    };

    return (
        <Drawer
            variant="permanent"
            sx={{
                width: 240,
                flexShrink: 0,
                '& .MuiDrawer-paper': {
                    width: 240,
                    boxSizing: 'border-box',
                    backgroundColor: '#f5f5f5', // Светло-серый фон
                },
            }}
        >
            <List>
                <ListItem button onClick={() => handleMenuClick('/')}>
                    <ListItemIcon>
                        <HomeIcon />
                    </ListItemIcon>
                    <ListItemText primary="Главная" />
                </ListItem>
                <Divider />
                <ListItem button onClick={() => handleToggle('reports')}>
                    <ListItemIcon>
                        <ReportIcon />
                    </ListItemIcon>
                    <ListItemText primary="Отчёты" />
                    {open.reports ? <ExpandLess /> : <ExpandMore />}
                </ListItem>
                    <Collapse in={open.reports} timeout="auto" unmountOnExit>
                        <List component="div" disablePadding>
                            <ListItem
                                button
                                sx={{ pl: 4 }} // Отступ для подпунктов
                                onClick={() => handleMenuClick('/ent_pretense_bill_stat')}
                            >
                                <ListItemIcon>
                                    <ReportIcon />
                                </ListItemIcon>
                                <ListItemText primary="Долговые" />
                            </ListItem>
                            <Collapse in={open.reports} timeout="auto" unmountOnExit>
                                <List component="div" disablePadding>
                                    <ListItem 
                                        button 
                                        sx={{ pl: 4 }}
                                        onClick={() => handleMenuClick('/report_quarterly_procuracy')} 
                                    >
                                        <ListItemIcon>
                                            <ReportIcon />
                                        </ListItemIcon>
                                        <ListItemText primary="Квартал" />
                                    </ListItem>
                                </List>
                            </Collapse>
                            <Collapse in={open.reports} timeout="auto" unmountOnExit>
                                <List component="div" disablePadding>
                                    <ListItem 
                                        button 
                                        sx={{ pl: 4 }}
                                        onClick={() => handleMenuClick('/report_weekly_rep')} 
                                    >
                                        <ListItemIcon>
                                            <ReportIcon />
                                        </ListItemIcon>
                                        <ListItemText primary="Неделя" />
                                    </ListItem>
                                </List>
                            </Collapse>
                        </List>
                    </Collapse>
                {/*<ListItem button onClick={() => handleMenuClick('/xml_parser')}>
                    <ListItemIcon>
                        <UploadFile />
                    </ListItemIcon>
                    <ListItemText primary="XML Parser" />
                </ListItem>*/}
            </List>
        </Drawer>
    );
};

export default SideMenu;
