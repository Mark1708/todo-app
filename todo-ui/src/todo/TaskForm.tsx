import React, { useState } from 'react';
import { Button, TextField, Box, Chip } from '@mui/material';
import {CreateTaskDto} from "../generated/models";
import {createTask} from "../generated/endpoints/task-controller/task-controller.ts";

interface TaskFormProps {
    onTaskCreated: () => void;
}

const TaskForm: React.FC<TaskFormProps> = ({ onTaskCreated }) => {
    const [title, setTitle] = useState<string>('');
    const [description, setDescription] = useState<string>('');
    const [tagInput, setTagInput] = useState<string>('');
    const [tags, setTags] = useState<string[]>([]);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        const newTask: CreateTaskDto = { title, description, done: false, tags };
        await createTask(newTask);
        setTitle('');
        setDescription('');
        setTags([]);
        onTaskCreated(); // Обновляем список задач
    };

    const handleAddTag = () => {
        if (tagInput && !tags.includes(tagInput)) {
            setTags([...tags, tagInput]);
            setTagInput('');
        }
    };

    const handleDeleteTag = (tagToDelete: string) => {
        setTags(tags.filter(tag => tag !== tagToDelete));
    };

    return (
        <Box component="form" onSubmit={handleSubmit} sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
            <TextField
                label="Title"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                required
            />
            <TextField
                label="Description"
                value={description}
                onChange={(e) => setDescription(e.target.value)}
            />
            <Box sx={{ display: 'flex', gap: 1, alignItems: 'center' }}>
                <TextField
                    label="Tag"
                    value={tagInput}
                    onChange={(e) => setTagInput(e.target.value)}
                />
                <Button variant="outlined" onClick={handleAddTag}>
                    Add Tag
                </Button>
            </Box>
            <Box sx={{ display: 'flex', gap: 1, flexWrap: 'wrap' }}>
                {tags.map((tag) => (
                    <Chip key={tag} label={tag} onDelete={() => handleDeleteTag(tag)} />
                ))}
            </Box>
            <Button type="submit" variant="contained" color="primary">
                Add Task
            </Button>
        </Box>
    );
};

export default TaskForm;
