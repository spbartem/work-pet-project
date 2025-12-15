import React from 'react';
import {
    Drawer,
    List,
    ListItem,
    ListItemIcon,
    ListItemText,
    Collapse,
    Divider,
    Box,
} from '@mui/material';
import {
    Home as HomeIcon,
    ExpandLess,
    ExpandMore,
    AssessmentOutlined as ReportIcon,
    DateRangeOutlined as WeeklyReportIcon,
    QueryStatsOutlined as QuarterlyReportIcon,
    ElderlyWomanOutlined as MonetaIcon,
    ImportExportOutlined as InfExchangeIcon
} from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';
import ExitToAppIcon from '@mui/icons-material/ExitToApp';

const SideMenu = () => {
    const navigate = useNavigate();
    const userRole = localStorage.getItem('role');

    const handleLogout = () => {
        localStorage.removeItem('token');
        localStorage.removeItem('role');
        navigate('/');
    };

    const [open, setOpen] = React.useState({
        reports: false,
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
                    backgroundColor: '#f5f5f5',
                    display: 'flex',
                    flexDirection: 'column',
                    justifyContent: 'space-between', // Чтобы нижний блок оказался внизу
                },
            }}
        >
            {/* Верхнее меню */}
            <Box>
                <List>
                    
                    <ListItem button onClick={() => handleMenuClick('/')}>
                        <ListItemIcon>
                            <HomeIcon />
                        </ListItemIcon>
                        <ListItemText primary="Главная" />
                    </ListItem>
                    <Divider />
                    <>
                        {['ADMIN', 'PAYMENT_CENTER'].includes(userRole) && (
                            <ListItem button onClick={() => handleToggle('reports')}>
                                <ListItemIcon>
                                    <ReportIcon />
                                </ListItemIcon>
                                <ListItemText primary="Отчёты" />
                                {open.reports ? <ExpandLess /> : <ExpandMore />}
                            </ListItem>
                        )}

                        <Collapse in={open.reports} timeout="auto" unmountOnExit>
                            <List component="div" disablePadding>
                                {/* <ListItem 
                                    button
                                    sx={{ pl: 4 }}
                                    onClick={() => handleMenuClick('/ent_pretense_bill_stat')}
                                >
                                    <ListItemIcon>
                                        <ReportIcon />
                                    </ListItemIcon>
                                    <ListItemText primary="Долговые" />
                                </ListItem>
                                */}
                                {['ADMIN'].includes(userRole) && (
                                    <ListItem
                                        button
                                        sx={{ pl: 4 }}
                                        onClick={() => handleMenuClick('/report_quarterly_procuracy')}
                                    >
                                        <ListItemIcon>
                                            <QuarterlyReportIcon />
                                        </ListItemIcon>
                                        <ListItemText primary="Квартал" />
                                    </ListItem>
                                )}
                                {['ADMIN', 'PAYMENT_CENTER'].includes(userRole) && (
                                    <ListItem
                                        button
                                        sx={{ pl: 4 }}
                                        onClick={() => handleMenuClick('/report_weekly_rep')}
                                    >
                                        <ListItemIcon>
                                            <WeeklyReportIcon />
                                        </ListItemIcon>
                                        <ListItemText primary="Неделя" />
                                    </ListItem>
                                )}
                            </List>
                        </Collapse>
                    </>
                    <Divider />
                    <>
                         {['ADMIN'].includes(userRole) && (
                            <ListItem button onClick={() => handleToggle('exchange')}>
                                <ListItemIcon>
                                    <InfExchangeIcon />
                                </ListItemIcon>
                                <ListItemText primary="Инф. обмен" />
                                {open.exchange ? <ExpandLess /> : <ExpandMore />}
                            </ListItem>
                        )}

                        <Collapse in={open.exchange} timeout="auto" unmountOnExit>
                            {['ADMIN'].includes(userRole) && (
                                <ListItem
                                    button
                                    sx={{ pl: 4}}
                                    onClick={() => handleMenuClick('/vckp_moneta')}
                                >
                                    <ListItemIcon>
                                        <MonetaIcon sx={{ color: 'purple' }}/>
                                    </ListItemIcon>
                                    <ListItemText primary="Монета" />
                                </ListItem>
                            )}
                        </Collapse>                               
                    </>
                </List>
            </Box>

            {/* Нижняя кнопка выхода */}
            <Box>
                <Divider />
                <List>
                    <ListItem button onClick={handleLogout}>
                        <ListItemIcon>
                            <ExitToAppIcon color="error" />
                        </ListItemIcon>
                        <ListItemText primary="Выйти" />
                    </ListItem>
                </List>
            </Box>
        </Drawer>
    );
};

export default SideMenu;
