import {defineConfig} from 'orval';

export default defineConfig({
    api: {
        hooks: {
            afterAllFilesWrite: 'prettier --write',
        },
        output: {
            clean: true,
            mode: 'tags-split',
            target: 'src/generated/endpoints',
            schemas: 'src/generated/models',
            client: 'fetch',
            baseUrl: 'http://localhost:8080',
            mock: false,
            override: {
                useNativeEnums: true,
            },
        },
        input: {
            target: 'http://localhost:8080/v3/api-docs',
        },
    },
});
