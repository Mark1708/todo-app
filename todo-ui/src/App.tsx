import {Container, Typography} from "@mui/material";
import TaskForm from "./todo/TaskForm.tsx";
import TaskList from "./todo/TaskList.tsx";
import {useEffect, useState} from "react";
import {TaskDto} from "./generated/models";
import {getAllTasks} from "./generated/endpoints/task-controller/task-controller.ts";


function App() {
    const [tasks, setTasks] = useState<TaskDto[]>([]);

    const fetchTasks = async () => {
        const response = await getAllTasks();
        if (response.status === 200) {
            setTasks(response.data);
        }
    };

    useEffect(() => {
        fetchTasks();
    }, []);

    const handleTaskCreated = () => {
        fetchTasks();
    };

    const handleTaskUpdated = () => {
        fetchTasks();
    };

    const handleTaskDeleted = (deletedTaskId: string) => {
        setTasks((prevTasks) => prevTasks.filter((task) => task.id !== deletedTaskId));
    };

    return (
        <Container>
            <Typography variant="h3" align="center" gutterBottom>
                Todo List
            </Typography>
            <TaskForm onTaskCreated={handleTaskCreated} />
            <TaskList tasks={tasks} onTaskUpdated={handleTaskUpdated} onTaskDeleted={handleTaskDeleted} />
        </Container>
    );
}

export default App
